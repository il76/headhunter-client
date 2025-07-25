# Фильтрация -- набор экранов фильтров поиска

Используя настройки фильтра, пользователь может уточнить некоторые параметры поиска, который осуществляется на экране
"Поиск". Фильтр позволяет указать:

- Место работы - регион, населённый пункт, указанный в вакансии как рабочая локация.
- Отрасль - сфера деятельности организации, разместившей вакансию.
- Уровень зарплаты - уровень ЗП, соответствующий указанному в вакансии.
- Возможность скрывать вакансии, для которых не указана ЗП.

## Особенности экранов

Несколько особенностей, которые нужно учитывать при реализации:

- Параметры фильтра не являются обязательными - пользователь может уточнить любой параметр из предложенных, а может не
  указывать ничего. В случае, если указан хотя бы один из параметров он должен учитываться при последующих поисковых
  запросах на экране "Поиск". Параметры фильтра, которые пользователь не уточнял, в поисковом запросе участвовать не
  должны.
- Настройки параметров фильтра должны сохраняться даже после закрытия приложения.
- Поиск по отраслям компании ведётся сразу по всем элементам дерева отраслей, без разделения на категории по уровням
  вложенности.
- Экраны фильтрации отображаются поверх нижней навигации.
- Если у пользователя выбрана страна поиска вакансий, то список регионов на экране выбора региона поиска
  ограничивается регионами указанной страны.
- Если у пользователя выбран регион поиска вакансий, то валюта поиска зарплаты соответствует этому региону.
- Если пользователь выбрал город до выбора страны, то страна должна подставляться автоматически. 
- Кнопка "Сбросить" появляется, если пользователь указал хотя бы одно значение фильтров.
- Кнопка "Применить" появляется, если пользователь указал фильтр, отличающийся от предыдущего. 

## Подсказки по декомпозиции

Что ещё можно уточнить в ходе декомпозиции макетов:

- Нужно ли при вводе ожидаемой зарплаты разбивать вводимое число на разряды?
- Можно ли указывать дробные числа при вводе ожидаемой зарплаты?
- Нужно ли запрещать ввод нескольких стартовых нулей в поле ожидаемой зарплаты?
- Как выглядит экран выбора региона, если мы получили пустой список в результате?
