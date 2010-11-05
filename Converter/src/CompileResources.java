/**************************************************************************
 Converter for Widnows MUI files.

 Copyright (C) 2010 Alex Buloichik <alex73mail@gmail.com>

 This program is free software; you can redistribute it and/or modify 
 it under the terms of the GNU General Public License as published by 
 the Free Software Foundation; either version 3 of the License, or 
 (at your option) any later version.

 This program is distributed in the hope that it will be useful, 
 but WITHOUT ANY WARRANTY; without even the implied warranty of 
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License 
 along with this program; if not, see http://www.gnu.org/licenses.
 **************************************************************************/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import muifile.ReaderWriterMUI;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import resources.CompilerMessageTable;
import resources.ParserRES;
import resources.RandomDataWriter;
import resources.ResUtils;
import resources.ResourceMessageTable;
import resources.io.WriterRC;
import resources.res.RESFile;
import win7.SkipResources;

/**
 * Гэты клясс абыходзіць .mui файлы ў каталёзе mui-bin/ і накладае на іх новыя
 * перакладзеныя рэсурсы.
 */
public class CompileResources {
    public static void main(String[] args) throws Exception {

        String sPath = "mui-bin/";
        String rcPath = "../i18n-bel/Windows7/target/";
        Map<String, File> files = ResUtils.listFiles(new File(sPath), "mui");

        for (Map.Entry<String, File> f : files.entrySet()) {
            System.err.println(f.getKey());

            File out = new File("out/" + f.getKey().replace("/en-US/", "/be-BY/"));
            out.getParentFile().mkdirs();
            try {
                go(f.getValue(), out, new File(rcPath + f.getKey().replaceAll("\\.mui$", ".rc")), f.getKey());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    static final byte[] LANG_ORIGINAL = "en-US".getBytes(ResUtils.UNICODE);
    static final byte[] LANG_LOCALIZED = "be-BY".getBytes(ResUtils.UNICODE);
    static final Pattern RE_MESSAGETABLE_HEADER = Pattern.compile("\\s*(\\d+)\\s+MESSAGETABLE\\s*");
    static final Pattern RE_MESSAGETABLE_LINE = Pattern.compile("\\s*([0-9\\-]+)\\s*,\\s*\"(.+)\"\\s*");

    static File tempResFile = new File("temp.res");
    static File tempRcFile = new File("temp.rc");
    static String tempMtPrefix = "tempmt";

    protected static void go(File inFile, File outFile, File rcFile, String file) throws Exception {
        tryRecreate(inFile, file);

        ReaderWriterMUI mui = new ReaderWriterMUI(FileUtils.readFileToByteArray(inFile));
        ResUtils.removeEmptyStrings(mui.getCompiledResources());

        // compile localized resources
        extractMT(rcFile, tempRcFile);
        exec("RC/rc.exe", "/iRC", "/d", "_UNICODE", "/d", "UNICODE", "/fo", tempResFile.getPath(), tempRcFile.getPath());
        /* "/g1","/fm", "/media/0082/temp.muires","/q","RC/config.rcconfig", */

        // read compiled resources
        RESFile localizedRes = new RESFile(tempResFile);

        // copy and fix MUI description from original file
        localizedRes.getResources().put("MUI", new TreeMap<Object, byte[]>());
        byte[] muiDesc = mui.getCompiledResources().get("MUI").get(1);
        muiDesc = fixMUI(muiDesc, LANG_ORIGINAL, LANG_LOCALIZED);
        localizedRes.getResources().get("MUI").put(1, muiDesc);

        // replace resource in MUI
        mui.replaceResources(1059, localizedRes.getResources());

        // save MUI
        RandomDataWriter out = new RandomDataWriter();
        mui.write(out);
        out.writeToFile(outFile);
    }

    /**
     * Try to recreate original file. Steps:
     * 
     * 1. Try to recreate DLL using binary resources from original file
     * 
     * 2. Extract original resources in text file
     * 
     * 3. Compile this text file
     * 
     * 4. Compare each-by-each binary resources from original file and from
     * compiled file
     * 
     * If original and created files are equals, we able to recreate file
     * without errors, so, we can create translated MUI.
     */
    protected static void tryRecreate(File origin, String fn) throws Exception {
        // read ofiginal MUI file
        byte[] originData = FileUtils.readFileToByteArray(origin);
        // parse binary DLL to separate binary resources
        ReaderWriterMUI mui = new ReaderWriterMUI(originData);

        // recreate DLL
        RandomDataWriter muiOut = new RandomDataWriter();
        mui.write(muiOut);

        // compare original and created DLLs
        byte[] dstFile = muiOut.getBytes();
        if (!SkipResources.SKIP_COMPARE_FILES.contains(fn)) {
            compare(originData, dstFile, "Files not equals !");
        }

        Map<Object, Map<Object, byte[]>> allBinRes = mui.getCompiledResources();
        ResUtils.removeEmptyStrings(allBinRes);

        Map<Object, Map<Object, byte[]>> forParseBinRes = SkipResources
                .minus(fn, allBinRes, SkipResources.SKIP_EXTRACT);
        forParseBinRes = SkipResources.minus(fn, forParseBinRes, SkipResources.SKIP_COMPARE);
        ParserRES res = new ParserRES(forParseBinRes);

        // dump resources to text file
        new WriterRC().write(res.getParsedResources(), tempRcFile);

        extractMT(tempRcFile, tempRcFile);
        // compile original text resources
        exec("RC/rc.exe", "/iRC", "/d", "_UNICODE", "/d", "UNICODE", "/fo", tempResFile.getPath(), tempRcFile.getPath());

        // read compiled resources
        RESFile originalRes = new RESFile(tempResFile);

        /*
         * // copy MUI description from original file
         * originalRes.getResources().put("MUI", new TreeMap<Object, byte[]>());
         * byte[] muiDesc = mui.getCompiledResources().get("MUI").get(1);
         * originalRes.getResources().get("MUI").put(1, muiDesc);
         */

        /*
         * // copy MESSAGETABLE from original file TODO if
         * (mui.getCompiledResources().containsKey(ResUtils.TYPE_MESSAGETABLE))
         * { originalRes.getResources().put(ResUtils.TYPE_MESSAGETABLE,
         * mui.getCompiledResources().get(ResUtils.TYPE_MESSAGETABLE)); }
         */

        checkExistAndCompare(forParseBinRes, originalRes.getResources());
    }

    /**
     * Extract and compile message tables from rc file.
     */
    protected static void extractMT(File rcFile, File outRcFile) throws Exception {
        byte[] rc = FileUtils.readFileToByteArray(rcFile);
        BufferedReader rd = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(rc), "UTF-8"));
        BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outRcFile), "UTF-8"));
        String s;
        while ((s = rd.readLine()) != null) {
            Matcher m = RE_MESSAGETABLE_HEADER.matcher(s);
            if (m.matches()) {
                int mtid = Integer.parseInt(m.group(1));
                ResourceMessageTable messages = new ResourceMessageTable();

                while (!(s = rd.readLine()).trim().equals("{"))
                    ;
                while ((s = rd.readLine()) != null) {
                    if (s.trim().equals("}")) {
                        break;
                    }
                    m = RE_MESSAGETABLE_LINE.matcher(s);
                    if (!m.matches()) {
                        throw new Exception("Invalid messagetable line: " + s);
                    }
                    long id = Long.parseLong(m.group(1));
                    String text = ResUtils.unescape(m.group(2));
                    messages.messages.put(id, text);
                }
                byte[] mt = CompilerMessageTable.compile(messages);
                File f = new File(tempMtPrefix + mtid);
                FileUtils.writeByteArrayToFile(f, mt);
                wr.write(mtid + " MESSAGETABLE " + f.getPath() + "\n");
            } else {
                wr.write(s + "\n");
            }
        }
        wr.close();
    }

    /**
     * check if all original resources exist in the localized file, and compare
     * they
     */
    protected static void checkExistAndCompare(Map<Object, Map<Object, byte[]>> r1, Map<Object, Map<Object, byte[]>> r2)
            throws Exception {
        for (Object e : r2.keySet()) {
            if (!r1.containsKey(e)) {
                System.err.println("There is no object type " + ResUtils.getObjectType(e) + " in original");
            }
        }
        for (Object e : r1.keySet()) {
            if (!r2.containsKey(e)) {
                System.err.println("There is no object type " + ResUtils.getObjectType(e) + " in compiled");
            }
        }

        for (Object e : r1.keySet()) {
            Map<Object, byte[]> enBin = r1.get(e);
            Map<Object, byte[]> enTxt = r2.get(e);
            if (enTxt != null) {
                for (Object in : enBin.keySet()) {
                    if (!enTxt.containsKey(in)) {
                        System.err.println("There is no object " + ResUtils.getObjectType(e) + "/" + in
                                + " in compiled");
                    } else {
                        compare(enBin.get(in), enTxt.get(in), "Different objects " + ResUtils.getObjectType(e) + "/"
                                + in + ": ");
                    }
                }
                for (Object in : enTxt.keySet()) {
                    if (!enBin.containsKey(in)) {
                        System.err.println("There is no object " + ResUtils.getObjectType(e) + "/" + in
                                + " in original");
                    }
                }
            }
        }
    }

    protected static void compare(byte[] d1, byte[] d2, String error) throws Exception {
        if (!Arrays.equals(d1, d2)) {
            FileUtils.writeByteArrayToFile(new File("d1"), d1);
            FileUtils.writeByteArrayToFile(new File("d2"), d2);
            Runtime.getRuntime().exec("sh ./hd.sh d1");
            Runtime.getRuntime().exec("sh ./hd.sh d2");
            System.err.println(error);
        }
    }

    /**
     * Replace "en-US" to "be-BY" in the "MUI" resource.
     */
    protected static byte[] fixMUI(byte[] in, byte[] find, byte[] replace) throws Exception {
        for (int i = 0; i < in.length - find.length; i++) {
            boolean found = true;
            for (int j = 0; j < find.length; j++) {
                if (in[i + j] != find[j]) {
                    found = false;
                    break;
                }
            }
            if (found) {
                byte[] r = new byte[in.length];
                System.arraycopy(in, 0, r, 0, in.length);
                System.arraycopy(replace, 0, r, i, replace.length);
                return r;
            }
        }
        throw new Exception("Can't fix MUI");
    }

    protected static void exec(String... cmd) throws Exception {
        List<String> rc = new ArrayList<String>(Arrays.asList(cmd));

        boolean isWin = System.getProperty("os.name").startsWith("Windows");
        if (!isWin) {
            rc.add(0, "wine");
        }
        String c = "";
        for (String r : rc) {
            c += r + ' ';
        }
        Process rcp = Runtime.getRuntime().exec(rc.toArray(new String[rc.size()]));
        int result = rcp.waitFor();
        if (result != 0) {
            System.err.println("===========================================");
            System.err.println("Error execute external command: " + c);
            IOUtils.copy(rcp.getInputStream(), System.err);
            IOUtils.copy(rcp.getErrorStream(), System.err);
            System.err.println("===========================================");
            throw new Exception("Error execute external command");
        }
    }
}
