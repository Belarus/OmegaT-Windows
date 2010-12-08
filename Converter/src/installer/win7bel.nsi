!include "MUI2.nsh"
!include "Registry.nsh"
!include "StrFunc.nsh"
!include "Library.nsh"

${StrRep}
;--------------------------------
;General

  ;Name and file
  Name "Беларускі пераклад Windows 7"
  OutFile "win7bel.exe"
  SetCompressor /SOLID lzma
  
  ;Request application privileges for Windows Vista
  RequestExecutionLevel admin

;--------------------------------
;Interface Settings

  !define MUI_ABORTWARNING


;--------------------------------
;Pages

  !insertmacro MUI_PAGE_LICENSE "readme.txt"
  !insertmacro MUI_PAGE_INSTFILES
  
  !insertmacro MUI_UNPAGE_WELCOME
  !insertmacro MUI_UNPAGE_INSTFILES  

;--------------------------------
;Languages
 
;  !insertmacro MUI_LANGUAGE "Belarusian"
!insertmacro MUI_INSERT
LoadLanguageFile "Belarusian.nlf"
!insertmacro LANGFILE_INCLUDE "Belarusian.nsh"

;--------------------------------

Var /GLOBAL FileBe
Var /GLOBAL RequiredVersion

Function VersionCheckFunc
    

    ${StrRep} $1 "$FileBe" "\be-BY\" "\en-US\"
    Push $1
    Pop $0
    IfFileExists $0 go
    ${StrRep} $2 "$FileBe" "\be-BY\" "\ru-RU\"
    Push $2
    Pop $0
    IfFileExists $0 go
        MessageBox MB_OK "Немагчыма ўсталяваць, бо не існуе ані файла $1, ані файла $2"
        Abort
go:
    GetDllVersion $0 $R0 $R1
    IntOp $R2 $R0 / 0x00010000
    IntOp $R3 $R0 & 0x0000FFFF
    IntOp $R4 $R1 / 0x00010000
    IntOp $R5 $R1 & 0x0000FFFF
    StrCpy $1 "$R2.$R3.$R4.$R5"

    StrCmp $1 $RequiredVersion done 0
        MessageBox MB_OK "Немагчыма ўсталяваць, бо файл $0 мае версію $1, але патрабуецца $RequiredVersion"
        Abort
done:
FunctionEnd

Function .onInit

    ${If} ${RunningX64}
##FILEVERSIONS64##
    ${Else}
##FILEVERSIONS32##
    ${EndIf}

FunctionEnd

;--------------------------------
;Installer Section

Section
    StrCpy $INSTDIR "$WINDIR\System32\be-BY\"

    ${If} ${RunningX64}
##DIR_INSTALL64##
    ${Else}
##DIR_INSTALL32##
    ${EndIf}

    WriteUninstaller "$INSTDIR\Uninstall.exe"
    WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\win7bel" "DisplayName" "$(^NameDA)"
    WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\win7bel" "UninstallString" "$\"$INSTDIR\Uninstall.exe$\""
    WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\win7bel" "DisplayVersion" "##DATE##"
    WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\win7bel" "HelpLink" "http://mounik.org/w/Windows"

    ${If} ${RunningX64}
##FILEUNPACK64##
##FILEINSTALL64##
    ${Else}
##FILEUNPACK32##
##FILEINSTALL32##
    ${EndIf}

    WriteRegDWORD       HKLM "SYSTEM\CurrentControlSet\Control\MUI\UILanguages\be-BY" "LCID" 0x00000423
    WriteRegStr         HKLM "SYSTEM\CurrentControlSet\Control\MUI\UILanguages\be-BY" "DefaultFallback" "en-US"
    WriteRegDWORD       HKLM "SYSTEM\CurrentControlSet\Control\MUI\UILanguages\be-BY" "Type" 0x00000094
    ${registry::Write} "HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Control\MUI\UILanguages\be-BY" "en-US" "" REG_MULTI_SZ $R0
    ${registry::Write} "HKEY_LOCAL_MACHINE\SYSTEM\CurrentControlSet\Control\MUI\UILanguages\be-BY" "ru-RU" "" REG_MULTI_SZ $R0

IfRebootFlag 0 NoReboot
  MessageBox MB_YESNO|MB_ICONQUESTION "Каб скончыць усталяванне, трэба перазапусціць камп'ютар. Зрабіць гэта зараз?" IDNO NoReboot
    Reboot
NoReboot:
SectionEnd
;--------------------------------
;Uninstaller Section

Section "Uninstall"

    ${If} ${RunningX64}
##DIR_UNINSTALL64##
    ${Else}
##DIR_UNINSTALL32##
    ${EndIf}


  DeleteRegKey  HKLM "SYSTEM\CurrentControlSet\Control\MUI\UILanguages\be-BY"
  DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\win7bel"

IfRebootFlag 0 NoReboot
  MessageBox MB_YESNO|MB_ICONQUESTION "Каб выдаліць некаторыя файлы, трэба перазапусціць камп'ютар. Зрабіць гэта зараз?" IDNO NoReboot
    Reboot
NoReboot:
SectionEnd
