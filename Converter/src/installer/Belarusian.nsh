;Language: Belarusian (1059)
;Translated by Sitnikov Vjacheslav [ glory_man@tut.by ]
;Little edition by eratyk for Windows 7 translation installer

!insertmacro LANGFILE "Belarusian" "Byelorussian"

!ifdef MUI_WELCOMEPAGE
  ${LangFileString} MUI_TEXT_WELCOME_INFO_TITLE "Вас вiтае майстар ўстаноўкі $(^NameDA)"
  ${LangFileString} MUI_TEXT_WELCOME_INFO_TEXT "Гэтая праграма ўсталюе $(^NameDA) на Ваш кампутар.$\r$\n$\r$\nПерад пачаткам устаноўкi прапануем зачыніць усе праграмы, якія выконваюцца ў сапраўдны момант. Гэта дапаможа праграме ўстаноўкі абнавіць сістэмныя файлы без перазагрузкі кампутара.$\r$\n$\r$\n$_CLICK"
!endif

!ifdef MUI_UNWELCOMEPAGE
  ${LangFileString} MUI_UNTEXT_WELCOME_INFO_TITLE "Вас вiтае майстар высталявання беларускага перакладу Windows 7"
  ${LangFileString} MUI_UNTEXT_WELCOME_INFO_TEXT "Гэтая праграма выдаліць беларускі пераклад Windows 7 з Вашага кампутара.$\r$\n$\r$\nПерад пачаткам выдалення пераканайцеся ў тым, што вы абралі іншую мову Windows 7.$\r$\n$\r$\n$_CLICK"
!endif

!ifdef MUI_LICENSEPAGE
  ${LangFileString} MUI_TEXT_LICENSE_TITLE "Усталяванне беларускага перакладу Winsows 7 ад ##DATE##"
  ${LangFileString} MUI_TEXT_LICENSE_SUBTITLE "Калі ласка, прачытайце інструкцыю па ўсталяванні."
  ${LangFileString} MUI_INNERTEXT_LICENSE_BOTTOM "Пасля прачытання інструкцыі па ўсталяванні пакунка націсніце кнопку $\"Усталяваць$\"."
  ${LangFileString} MUI_INNERTEXT_LICENSE_BOTTOM_CHECKBOX "Калi Вы прымаеце ўмовы Лiцензiйнага пагаднення, усталюйце сцяжок ніжэй. Гэта неабходна для ўстаноўкі праграмы. $_CLICK"
  ${LangFileString} MUI_INNERTEXT_LICENSE_BOTTOM_RADIOBUTTONS "Калi Вы прымаеце ўмовы Ліцэнзійнага пагаднення, вылучыце першы варыянт з прапанованых ніжэй. Гэта неабходна для ўстаноўкі праграмы. $_CLICK"
!endif

!ifdef MUI_UNLICENSEPAGE
  ${LangFileString} MUI_UNTEXT_LICENSE_TITLE "Інструкцыя па ўсталяванні"
  ${LangFileString} MUI_UNTEXT_LICENSE_SUBTITLE "Калі ласка, прачытайце ўмовы Ліцэнзійнага пагаднення перад пачаткам выдалення $(^NameDA)."
  ${LangFileString} MUI_UNINNERTEXT_LICENSE_BOTTOM "Калі Вы жадаеце высталяваць праграму, націсніце кнопку "Высталяваць". $_CLICK"
  ${LangFileString} MUI_UNINNERTEXT_LICENSE_BOTTOM_CHECKBOX "Калі Вы прымаеце ўмовы Ліцэнзійнага пагаднення, усталюйце сцяжок ніжэй. Гэта неабходна для выдалення праграмы. $_CLICK"
  ${LangFileString} MUI_UNINNERTEXT_LICENSE_BOTTOM_RADIOBUTTONS "Калі Вы прымаеце ўмовы Ліцэнзійнага пагаднення, вылучце першы варыянт з прапанаваных ніжэй. Гэта неабходна для выдалення праграмы. $_CLICK"
!endif

!ifdef MUI_LICENSEPAGE | MUI_UNLICENSEPAGE
  ${LangFileString} MUI_INNERTEXT_LICENSE_TOP "Карыстайце кнопкi $\"PageUp$\" i $\"PageDown$\" для перамяшчэння па тэксце."
!endif

!ifdef MUI_COMPONENTSPAGE
  ${LangFileString} MUI_TEXT_COMPONENTS_TITLE "Кампаненты праграмы, якая ўсталёўваецца"
  ${LangFileString} MUI_TEXT_COMPONENTS_SUBTITLE "Вызначце кампаненты $(^NameDA), якія Вы жадаеце ўсталяваць."
!endif

!ifdef MUI_UNCOMPONENTSPAGE
  ${LangFileString} MUI_UNTEXT_COMPONENTS_TITLE "Кампаненты праграмы"
  ${LangFileString} MUI_UNTEXT_COMPONENTS_SUBTITLE "Вызначце кампаненты $(^NameDA), якія Вы жадаеце выдаліць."
!endif

!ifdef MUI_COMPONENTSPAGE | MUI_UNCOMPONENTSPAGE
  ${LangFileString} MUI_INNERTEXT_COMPONENTS_DESCRIPTION_TITLE "Апісанне"
  !ifndef NSIS_CONFIG_COMPONENTPAGE_ALTERNATIVE
    ${LangFileString} MUI_INNERTEXT_COMPONENTS_DESCRIPTION_INFO "Усталюйце курсор мышы на назву кампанента, каб прачытаць яго апісанне."
  !else
    ${LangFileString} MUI_INNERTEXT_COMPONENTS_DESCRIPTION_INFO "Усталюйце курсор мышы на назву кампанента, каб прачытаць яго апісанне."
  !endif
!endif

!ifdef MUI_DIRECTORYPAGE
  ${LangFileString} MUI_TEXT_DIRECTORY_TITLE "Выбар папкі ўстаноўкі"
  ${LangFileString} MUI_TEXT_DIRECTORY_SUBTITLE "Вызначце папку для ўстаноўкі $(^NameDA)."
!endif

!ifdef MUI_UNDIRECTORYPAGE
  ${LangFileString} MUI_UNTEXT_DIRECTORY_TITLE "Выбар папкі для выдалення"
  ${LangFileString} MUI_UNTEXT_DIRECTORY_SUBTITLE "Вызначце папку, з якой патрэбна выдаліць $(^NameDA)."
!endif

!ifdef MUI_INSTFILESPAGE
  ${LangFileString} MUI_TEXT_INSTALLING_TITLE "Капіяванне файлаў"
  ${LangFileString} MUI_TEXT_INSTALLING_SUBTITLE "Пачакайце, калі ласка, выконваецца капіраванне файлаў $(^NameDA) на Ваш кампутар..."
  ${LangFileString} MUI_TEXT_FINISH_TITLE "Устаноўка завершана"
  ${LangFileString} MUI_TEXT_FINISH_SUBTITLE "Устаноўка паспяхова завершана."
  ${LangFileString} MUI_TEXT_ABORT_TITLE "Устаноўка перарвана"
  ${LangFileString} MUI_TEXT_ABORT_SUBTITLE "Устаноўка не завершана."
!endif

!ifdef MUI_UNINSTFILESPAGE
  ${LangFileString} MUI_UNTEXT_UNINSTALLING_TITLE "Выдаленне"
  ${LangFileString} MUI_UNTEXT_UNINSTALLING_SUBTITLE "Пачакайце, калі ласка, выконваецца выдаленне файлаў $(^NameDA) з Вашага кампутара..."
  ${LangFileString} MUI_UNTEXT_FINISH_TITLE "Выдаленне завершана"
  ${LangFileString} MUI_UNTEXT_FINISH_SUBTITLE "Выдаленне праграмы паспяхова завершана."
  ${LangFileString} MUI_UNTEXT_ABORT_TITLE "Выдаленне перарвана"
  ${LangFileString} MUI_UNTEXT_ABORT_SUBTITLE "Выдаленне выканана не поўнасцю."
!endif

!ifdef MUI_FINISHPAGE
  ${LangFileString} MUI_TEXT_FINISH_INFO_TITLE "Заканчэнне ўсталёўкі $(^NameDA)"
  ${LangFileString} MUI_TEXT_FINISH_INFO_TEXT "Усталёўка $(^NameDA) выканана.$\r$\n$\r$\nНацісніце кнопку $\"Гатова$\" для выйсця з праграмы ўсталёўкі."
  ${LangFileString} MUI_TEXT_FINISH_INFO_REBOOT "Каб скончыць усталяванне $(^NameDA), неабходна перазапусціць камп'ютар. Жадаеце зрабіць гэта зараз?"
!endif

!ifdef MUI_UNFINISHPAGE
  ${LangFileString} MUI_UNTEXT_FINISH_INFO_TITLE "Заканчэнне высталёўкі $(^NameDA)"
  ${LangFileString} MUI_UNTEXT_FINISH_INFO_TEXT "Праграма $(^NameDA) высталявана з Вашага кампутара.$\r$\n$\r$\nНацісніце кнопку $\"Гатова$\"каб выйсці з праграмы выдалення."
  ${LangFileString} MUI_UNTEXT_FINISH_INFO_REBOOT "Каб скончыць высталяванне $(^NameDA), неабходна перазапусціць камп'ютар. Жадаеце зрабіць гэта зараз?"
!endif

!ifdef MUI_FINISHPAGE | MUI_UNFINISHPAGE
  ${LangFileString} MUI_TEXT_FINISH_REBOOTNOW "Так, перазапусціць камп'ютар зараз"
  ${LangFileString} MUI_TEXT_FINISH_REBOOTLATER "Не, перазапусціць камп'ютар пазней"
  ${LangFileString} MUI_TEXT_FINISH_RUN "&Запусціць $(^NameDA)"
  ${LangFileString} MUI_TEXT_FINISH_SHOWREADME "&Паказаць інфармацыю аб праграме"
  ${LangFileString} MUI_BUTTONTEXT_FINISH "&Гатова"  
!endif

!ifdef MUI_STARTMENUPAGE
  ${LangFileString} MUI_TEXT_STARTMENU_TITLE "Папка ў меню $\"Пуск$\""
  ${LangFileString} MUI_TEXT_STARTMENU_SUBTITLE "Вылучыце папку ў меню $\"Пуск$\" для размяшчэння ярлыкоў праграмы."
  ${LangFileString} MUI_INNERTEXT_STARTMENU_TOP "Вылучыце папку ў меню $\"Пуск$\", куды будуць змешчаны ярлыкі праграмы. Вы таксама можаце азначыць іншае імя папкі."
  ${LangFileString} MUI_INNERTEXT_STARTMENU_CHECKBOX "Не ствараць ярлыкі"
!endif

!ifdef MUI_UNCONFIRMPAGE
  ${LangFileString} MUI_UNTEXT_CONFIRM_TITLE "Выдаленне $(^NameDA)"
  ${LangFileString} MUI_UNTEXT_CONFIRM_SUBTITLE "Выдаленне $(^NameDA) з Вашага кампутара."
!endif

!ifdef MUI_ABORTWARNING
  ${LangFileString} MUI_TEXT_ABORTWARNING "Вы сапраўды жадаеце скасаваць усталяванне беларускага перакладу Windows 7?"
!endif

!ifdef MUI_UNABORTWARNING
  ${LangFileString} MUI_UNTEXT_ABORTWARNING "Вы сапраўды жадаеце скасаваць высталяванне беларускага перакладу Windows 7?"
!endif
