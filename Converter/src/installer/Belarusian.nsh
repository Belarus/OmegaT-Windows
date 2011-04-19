;Language: Belarusian (1059)
;Translated by Sitnikov Vjacheslav [ glory_man@tut.by ]
;Little edition by eratyk for Windows 7 translation installer

!insertmacro LANGFILE "Belarusian" "Byelorussian"

!ifdef MUI_WELCOMEPAGE
  ${LangFileString} MUI_TEXT_WELCOME_INFO_TITLE "Вас вiтае майстар усталявання беларускага перакладу Windows 7"
  ${LangFileString} MUI_TEXT_WELCOME_INFO_TEXT "Гэтая праграма ўсталюе $(^NameDA) на Ваш камп'ютар.$\r$\n$\r$\nПерад пачаткам усталявання прапануем зачыніць усе праграмы, якія зараз працуюць. Гэта дапаможа ўсталёўніку абнавіць сістэмныя файлы без перазапуску камп'ютара.$\r$\n$\r$\n$_CLICK"
!endif

!ifdef MUI_UNWELCOMEPAGE
  ${LangFileString} MUI_UNTEXT_WELCOME_INFO_TITLE "Вас вiтае майстар высталявання беларускага перакладу Windows 7"
  ${LangFileString} MUI_UNTEXT_WELCOME_INFO_TEXT "Гэтая праграма высталюе беларускі пераклад Windows 7 з Вашага камп'ютара.$\r$\n$\r$\nПерад пачаткам высталёўвання пераканайцеся ў тым, што Вы абралі іншую мову Windows 7.$\r$\n$\r$\n$_CLICK"
!endif

!ifdef MUI_LICENSEPAGE
  ${LangFileString} MUI_TEXT_LICENSE_TITLE "Усталяванне беларускага перакладу Winsows 7 ад ##DATE##"
  ${LangFileString} MUI_TEXT_LICENSE_SUBTITLE "Калі ласка, прачытайце інструкцыю па ўсталяванні."
  ${LangFileString} MUI_INNERTEXT_LICENSE_BOTTOM "Пасля прачытання інструкцыі па ўсталяванні пакунка націсніце кнопку $\"Усталяваць$\"."
  ${LangFileString} MUI_INNERTEXT_LICENSE_BOTTOM_CHECKBOX "Калi Вы прымаеце ўмовы лiцензiйнага пагаднення, пастаўце птушку ніжэй. Гэта неабходна для усталявання пакунка. $_CLICK"
  ${LangFileString} MUI_INNERTEXT_LICENSE_BOTTOM_RADIOBUTTONS "Калi Вы прымаеце ўмовы ліцэнзійнага пагаднення, абярыце першы варыянт з прапанаваных ніжэй. Гэта неабходна для ўсталявання пакунка. $_CLICK"
!endif

!ifdef MUI_UNLICENSEPAGE
  ${LangFileString} MUI_UNTEXT_LICENSE_TITLE "Інструкцыя па ўсталяванні"
  ${LangFileString} MUI_UNTEXT_LICENSE_SUBTITLE "Калі ласка, прачытайце ўмовы ліцэнзійнага пагаднення перад пачаткам высталявання беларускага перакладу Windows 7."
  ${LangFileString} MUI_UNINNERTEXT_LICENSE_BOTTOM "Калі Вы жадаеце высталяваць пакунак, націсніце кнопку "Высталяваць". $_CLICK"
  ${LangFileString} MUI_UNINNERTEXT_LICENSE_BOTTOM_CHECKBOX "Калі Вы прымаеце ўмовы ліцэнзійнага пагаднення, усталюйце сцяжок ніжэй. Гэта неабходна для высталявання пакунка. $_CLICK"
  ${LangFileString} MUI_UNINNERTEXT_LICENSE_BOTTOM_RADIOBUTTONS "Калі Вы прымаеце ўмовы ліцэнзійнага пагаднення, абярыце першы варыянт з прапанаваных ніжэй. Гэта неабходна для выдалення пакунка. $_CLICK"
!endif

!ifdef MUI_LICENSEPAGE | MUI_UNLICENSEPAGE
  ${LangFileString} MUI_INNERTEXT_LICENSE_TOP "Карыстайце кнопкi $\"PageUp$\" i $\"PageDown$\" для персоўвання па тэксце."
!endif

!ifdef MUI_COMPONENTSPAGE
  ${LangFileString} MUI_TEXT_COMPONENTS_TITLE "Кампаненты пакунка для ўсталявання"
  ${LangFileString} MUI_TEXT_COMPONENTS_SUBTITLE "Азначце кампаненты беларускага перакладу Windows 7, якія Вы жадаеце ўсталяваць."
!endif

!ifdef MUI_UNCOMPONENTSPAGE
  ${LangFileString} MUI_UNTEXT_COMPONENTS_TITLE "Кампаненты пакунка"
  ${LangFileString} MUI_UNTEXT_COMPONENTS_SUBTITLE "Азначце кампаненты беларускага перакладу Windows 7, якія Вы жадаеце высталяваць."
!endif

!ifdef MUI_COMPONENTSPAGE | MUI_UNCOMPONENTSPAGE
  ${LangFileString} MUI_INNERTEXT_COMPONENTS_DESCRIPTION_TITLE "Апісанне"
  !ifndef NSIS_CONFIG_COMPONENTPAGE_ALTERNATIVE
    ${LangFileString} MUI_INNERTEXT_COMPONENTS_DESCRIPTION_INFO "перасуньце паказальнік мышы на назву кампанента, каб прачытаць ягонае апісанне."
  !else
    ${LangFileString} MUI_INNERTEXT_COMPONENTS_DESCRIPTION_INFO "Перасуньце паказальнік мышы на назву кампанента, каб прачытаць ягонае апісанне."
  !endif
!endif

!ifdef MUI_DIRECTORYPAGE
  ${LangFileString} MUI_TEXT_DIRECTORY_TITLE "Абранне каталога ўсталявання"
  ${LangFileString} MUI_TEXT_DIRECTORY_SUBTITLE "Азначце каталог для ўсталявання беларускага перакладу Windows 7."
!endif

!ifdef MUI_UNDIRECTORYPAGE
  ${LangFileString} MUI_UNTEXT_DIRECTORY_TITLE "Абранне каталога для высталявання"
  ${LangFileString} MUI_UNTEXT_DIRECTORY_SUBTITLE "Азначце каталог, з якога трэба высталояваць $(^NameDA)."
!endif

!ifdef MUI_INSTFILESPAGE
  ${LangFileString} MUI_TEXT_INSTALLING_TITLE "Капіяванне файлаў"
  ${LangFileString} MUI_TEXT_INSTALLING_SUBTITLE "Пачакайце, калі ласка, выконваецца капіяванне файлаў беларускага перакладу Windows 7 на Ваш камп'ютар..."
  ${LangFileString} MUI_TEXT_FINISH_TITLE "Усталяванне завершанае"
  ${LangFileString} MUI_TEXT_FINISH_SUBTITLE "Усталяванне паспяхова завершанае."
  ${LangFileString} MUI_TEXT_ABORT_TITLE "Усталяванне перарванае"
  ${LangFileString} MUI_TEXT_ABORT_SUBTITLE "Усталяванне не завершанае."
!endif

!ifdef MUI_UNINSTFILESPAGE
  ${LangFileString} MUI_UNTEXT_UNINSTALLING_TITLE "Высталяванне"
  ${LangFileString} MUI_UNTEXT_UNINSTALLING_SUBTITLE "Пачакайце, калі ласка, выконваецца выдаленне файлаў $(^NameDA) з Вашага камп'ютара..."
  ${LangFileString} MUI_UNTEXT_FINISH_TITLE "Высталяванне завершанае"
  ${LangFileString} MUI_UNTEXT_FINISH_SUBTITLE "Высталяванне пакунка паспяхова завершанае."
  ${LangFileString} MUI_UNTEXT_ABORT_TITLE "Высталяванне перарванае"
  ${LangFileString} MUI_UNTEXT_ABORT_SUBTITLE "Высталяванне выкананае не поўнасцю."
!endif

!ifdef MUI_FINISHPAGE
  ${LangFileString} MUI_TEXT_FINISH_INFO_TITLE "Заканчэнне ўсталявання беларускага перакладу Windows 7"
  ${LangFileString} MUI_TEXT_FINISH_INFO_TEXT "Усталяванне беларускага перакладу Windows 7 выкананае.$\r$\n$\r$\nНацісніце кнопку $\"Гатова$\" для выйсця з усталёўніка."
  ${LangFileString} MUI_TEXT_FINISH_INFO_REBOOT "Каб скончыць усталяванне беларускага перакладу Windows 7, неабходна перазапусціць камп'ютар. Зрабіць гэта зараз?"
!endif

!ifdef MUI_UNFINISHPAGE
  ${LangFileString} MUI_UNTEXT_FINISH_INFO_TITLE "Заканчэнне высталявання беларускага перакладу Windows 7"
  ${LangFileString} MUI_UNTEXT_FINISH_INFO_TEXT "Пакунак беларускага перакладу Windows 7 высталяваны з Вашага камп'ютара.$\r$\n$\r$\nНацісніце кнопку $\"Гатова$\", каб выйсці з высталёўніка."
  ${LangFileString} MUI_UNTEXT_FINISH_INFO_REBOOT "Каб скончыць высталяванне беларускага перакладу Windows 7, неабходна перазапусціць камп'ютар. Жадаеце зрабіць гэта зараз?"
!endif

!ifdef MUI_FINISHPAGE | MUI_UNFINISHPAGE
  ${LangFileString} MUI_TEXT_FINISH_REBOOTNOW "Так, перазапусціць камп'ютар зараз"
  ${LangFileString} MUI_TEXT_FINISH_REBOOTLATER "Не, перазапусціць камп'ютар пазней"
  ${LangFileString} MUI_TEXT_FINISH_RUN "Запусціць $(^NameDA)"
  ${LangFileString} MUI_TEXT_FINISH_SHOWREADME "Паказаць інфармацыю аб пакунку"
  ${LangFileString} MUI_BUTTONTEXT_FINISH "&Гатова"  
!endif

!ifdef MUI_STARTMENUPAGE
  ${LangFileString} MUI_TEXT_STARTMENU_TITLE "Каталог ў Пачатковае меню"
  ${LangFileString} MUI_TEXT_STARTMENU_SUBTITLE "Вылучыце каталог ў Пачатковым меню для размяшчэння пускальнікаў пакунка."
  ${LangFileString} MUI_INNERTEXT_STARTMENU_TOP "Вылучыце каталог ў Пачатковым меню для размяшчэння пускальнікаў пакунка. Вы таксама можаце азначыць іншую назву каталога."
  ${LangFileString} MUI_INNERTEXT_STARTMENU_CHECKBOX "Не ствараць пускальнікі"
!endif

!ifdef MUI_UNCONFIRMPAGE
  ${LangFileString} MUI_UNTEXT_CONFIRM_TITLE "Высталяванне беларускага перакладу Windows 7"
  ${LangFileString} MUI_UNTEXT_CONFIRM_SUBTITLE "Высталяванне беларускага перакладу Windows 7 з Вашага камп'ютара."
!endif

!ifdef MUI_ABORTWARNING
  ${LangFileString} MUI_TEXT_ABORTWARNING "Вы сапраўды жадаеце скасаваць усталяванне беларускага перакладу Windows 7?"
!endif

!ifdef MUI_UNABORTWARNING
  ${LangFileString} MUI_UNTEXT_ABORTWARNING "Вы сапраўды жадаеце скасаваць высталяванне беларускага перакладу Windows 7?"
!endif
