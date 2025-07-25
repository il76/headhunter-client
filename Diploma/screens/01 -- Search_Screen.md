# Главный экран -- экран поиска вакансий

На этом экране пользователь может искать вакансии по любому непустому набору слов поискового запроса. Результаты поиска
представляют собой список, содержащий краткую информацию о вакансиях.

## Особенности экрана

Несколько особенностей экрана, которые нужно учитывать при реализации:

- По умолчанию, поиск происходит по всей доступной базе вакансий без учёта региона, отрасли компании и уровня зарплаты и
  валюты.
- Приложение не хранит историю поиска, поэтому между перезапусками приложения текст в поле ввода не обязан сохраняться.
- При вводе нового текста в поле ввода мы осуществляем новый поиск с debounce в 300 миллисекунд.
- В отдельном элементе списка может быть картинка логотипа компании, которую нужно дополнительно загрузить. В процессе
  загрузки картинки и в случае ошибки загрузки этой картинки мы показываем плейсхолдер. Также плейсхолдер отображается,
  если информации о картинке нет.
- В зависимости от пришедших с сервера данных информация о вакансии может отображаться несколькими способами:
    - "XX"
    - "От XX";
    - "До XX";
    - "От XX до XX";
    - "Зарплата не указана".
- Если в вакансии указана зарплата, то числа должны отображаться с разбиением на разряды (то
  есть `1 000 000`, `12 345 678`).
- Зарплата в вакансии может быть указана в разной валюте, не только в рублях. Вот полный список возможных валют:
    - Российский рубль (RUR / RUB)
    - Белорусский рубль (BYR)
    - Доллар (USD)
    - Евро (EUR)
    - Казахстанский тенге (KZT)
    - Украинская гривна (UAH)
    - Азербайджанский манат (AZN)
    - Узбекский сум (UZS)
    - Грузинский лари (GEL)
    - Киргизский сом (KGT)
- (\*) В целях экономии трафика пользователей загрузка результатов поиска должна происходить постранично (paging) по 20
  элементов за раз. Запрос на следующую страницу должен происходить, когда пользователь доскроллил до последнего
  доступного элемента списка (или чуть раньше).

## Подсказки по декомпозиции

Что ещё можно уточнить в ходе декомпозиции макета:

- Как выглядит плейсхолдер логотипа компании, если идёт загрузка картинки или произошла ошибка загрузки логотипа?
- Как выглядит экран, если у нас выдача пустая (нет результатов или запрос ещё не сделан)?
- Как выглядит выдача, если при запросе ПЕРВОЙ страницы случилась ошибка?
- Как выглядит выдача, когда у нас при запросе ВТОРОЙ ИЛИ ПОСЛЕДУЮЩИХ страниц случились ошибки?
- Как выглядит экран загрузки выдачи при запросе ПЕРВОЙ страницы?
- Нужно ли при отображении числа найденных вакансий разбивать число на разряды?
