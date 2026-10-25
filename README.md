BUDGETHANTERAREN

Programmet låter en användare hantera sin budget genom att lägga till transaktioner i form av
inkomster och utgifter. Användaren avgör själv vad som är en inkomst eller utgift, 
vilken kategori (t.ex. 'Mat') transaktionen tillhör samt belopp. 
Programmet är i form av en interaktiv meny vilket möjliggör att användaren kan utföra
flera olika val. Som att t.ex. visa aktuellt saldo, visa en sammanställning av transaktioner per kategori etc.
Om användaren vill filtrera transaktioner utifrån typ (income/expense), datum eller kategori så tas användaren
först till en sub meny. Där kan användaren välja vilken typ av filtrering som ska utföras. Slutligen kan användaren
välja att spara alla inmatade transaktioner till en csv-fil i main menyn innan användaren avslutar programmet.

Funktioner
* Lägga till transaktioner
* Visa saldo
* Filtrera data
* Stream API & Generics
* CSV-lagring

Programmet använder collections framework (List och Map) samt streams. 
Vidare är Repository en generisk klass men som i detta program enbart används för att 
hantera transaktioner. Alla dess metoder är generiska och returnerar alltid en generisk typ.
Så om man hade velat göra programmet User-specifik med t.ex. användare ID så kan Repository 
återanvändas för att hantera detta. Ett till Repository hade då skapats där <T> istället sätts 
till typ 'User' (t.ex. ett User record med namn, Id o.s.v.) istället för <T> -> Transaction.
Klassens metoder blir då även återanvändbara, till exempel för att lägga till olika användare i en lista.
Slutligen hade varje användare mappats till en lista med egna transaktioner (detta hade dock behövas implementeras).

Undantagshantering implementerades med try-catch-blocks där det behövdes, både för att fånga Javas egna exceptions samt mitt egna 
skapade exception. Användningen av stream-API:et förenklade logiken enormt och gjorde det mycket lättare att både 
skriva och läsa koden jämfört med Laboration 1 där man var tvungen att använda vanliga for-loops.
Hela BudgetService-klassen använder i princip enbart streams för all beräkningslogik och operationer.
IntelliJ 'annoterar' stream-operationer med t.ex. Stream<Transaction> eller DoubleStream vilket gör
det lätt att förstå vad som returneras från varje stream-operation. Så med debugging var det enkelt att felsöka
programmet.

Vidare användes loggning samt debugging i samband med JUnit-testerna. Loggningen har flera nivåer bl.a.
warning, info och severe. <info> -> När t.ex. transaktioner läggs till och för att bekräfta rätt inmatning av data
o.s.v. <warning> -> När fel inmatning skett, <severe> -> När fel med fil-läsning och fil-sparning uppstår.   

I samband med JUnit-testerna upptäcktes ett par fel då vissa testfall misslyckades. 
Dessa sammanfattas här nedan, d.v.s. Vilket test som misslyckades, orsaken, felsökning samt lösningen.

* Problem:

JUnit-testet getCategorySumShouldSumItemsFromTheSameCategory() misslyckades.

Förväntat: 30000. Fick: null

Orsak: Nycklarna i mapen som returneras var case-sensitive.  

Felsökning: Breakpoint sattes i getCategorySum(). Debugger användes stegvis och variabler inspekterades.

Lösning: Bytte stream-operator (rad 39 i BudgetService) där transaktionerna grupperas utifrån kategori från 
Transaction::category till transaction -> transaction.category().toLowerCase(). Detta resulterade i att 
den returnerade mapens keys enbart innehåller små bokstäver. Testet lyckades därefter. Syftet med denna ändring
är att säkerställa att kategorinamnen är lika oavsett om de är skrivna med stora eller små bokstäver. 

* Problem:

JUnit-testet getByCategoryShouldReturnOnlyTransactionsForGivenCategory() misslyckades.

Förväntat: 1. Fick: 0

Orsak: Funktionen getByCategory() är case-sensitive.

Felsökning: Breakpoint sattes i getByCategory(). Debugger användes stegvis och variabler inspekterades.

Lösning: Ändrade stream-operator från transaction -> transaction.category().equals(category) till 
transaction -> transaction.category().equalsIgnoreCase(category). Testet lyckades därefter. Den här ändringen 
är viktig för att säkerställa att användaren kan söka efter transaktioner utan att kategorinamnet är 
beroende av stora eller små bokstäver. Detta gör att användaren kan söka efter kategorier på ett 
flexibelt sätt och förbättrar användarvänligheten.

Reflektion:

Implementerade logger först när main klassen byggdes. Eftersom det är i main som majoriteten av programmets 
inmatning från användaren sker så är det viktigt att logga användarinteraktioner och eventuella fel som 
uppkommer under användningen av programmet. Logger hjälpte till att felsöka problem och förbättrade
användarvänligheten genom att ge insikter om hur programmet fungerar och vilka användarinteraktioner som utförs.
Där det gick byggdes programmet på ett sätt för att minimera behovet av felhantering som t.ex. vid menyvalen
där scanner.hasNextInt() används istället för try-catch med ParseInt(). Vidare vid inmatningen av amount
användes scanner.hasNextDouble() istället för try-catch med ParseDouble() för att minimera behovet av felhantering.
I framtida programmering kan det vara användbart att implementera logger direkt i början samt kanske börja med main metoden
för att få en bättre överblick över programmet och dess funktioner, även om funktion anropen enbart då består av 'skal'.

Den egna implementerade InvalidTransactionException-klassen används för att hantera fel som 
uppstår när användaren försöker lägga till en transaktion med ogiltiga värden (0 eller negativt amount). 
Detta hjälper till att förbättra användarvänligheten genom att ge användaren tydliga meddelanden om vad som är fel 
och hur de kan åtgärda det. En möjlig ändring är att istället lägga till en egen konstruktor i Transaction klassen
där man kontrollerar att amount är större än 0 annars kastas InvalidTransactionException. En transaktion hade alltså
inte då tillåtits att skapas vid fel amount-värde. Denna kontroll sker just nu i addTransaction-metoden 
i BudgetService-klassen, vilket innebär att ett transaktion-object med fel amount-value 'får' skapas, men tillåts inte
att läggas till i repositoryn senare istället.

Slutligen implementerades filhanteringen. Klassen FileService används för att hantera filer som 
används för att spara och ladda budgetdata. Den innehåller både en metod för att spara budgetdata till en fil 
samt en metod för att läsa budgetdata från en fil. Logger används för att logga information om filhanteringen, 
så att användaren kan se vad som händer när filer sparas och laddas. Undantagen som kan kastas av FileService hanteras
dock inte i den egna klassen, utan det skickas upp och hanteras i Main. Här hade jag kunnat skapa ytterligare en egen
exception-klass som istället extender IOException som hade till exempel kunnat kallas FileHandlingException. 

Förbättringar som kan göras:

Vidare är inte programmet helt anpassat för egna manuella ändringar i filen som ska läsas in.
Det var inte helt tydligt i uppgiftsbeskrivningen (rimlig felhantering). Det går t.ex. i dagsläget att sätta amount till ett
negativt värde, manuellt i filen. Men en enkel lösning för specifikt det problemet är att implementera mitt egna InvalidTransactionException
i Transaction-klassens konstruktor istället, vilket jag nämnde tidigare. Ett annat problem är att det går att skriva in framtida datum
(så länge de är giltiga) manuellt i filen, vilket inte är "tillåtet". Ogiltiga datum hanteras dock fint på grund av LocalDate.parse, 
därav så hade ytterligare en egen exception-klass kunnat implementeras här med t.ex. namnet FutureDateException.




