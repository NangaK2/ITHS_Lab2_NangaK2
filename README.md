BUDGETHANTERAREN

Programmet låter en användare hantera sin budget genom att lägga till transaktioner i form av
inkomster och utgifter. Användaren avgör själv vad som är en inkomst eller utgift, 
vilken kategori (t.ex. 'Mat') transaktionen tillhör samt belopp. 
Programmet är i form av en interaktiv meny vilket möjliggör att användaren kan utföra
flera olika val. Som att t.ex. visa aktuellt saldo, visa en sammanställning av transaktioner per kategori 
samt filtrera transaktioner utifrån typ (inkomst eller utgift) eller datum. Slutligen kan användaren
spara transaktionerna till en csv-fil.

Funktioner
* Lägga till transaktioner
* Visa saldo
* Filtrera data
* Stream API & Generics
* CSV-lagring

Problem:
JUnit-testet getCategorySumShouldSumItemsFromTheSameCategory() misslyckades.

Förväntat: 30000. Fick: null

Orsak: Nycklarna i mapen som returneras var case-sensitive.  

Felsökning: Breakpoint sattes i getCategorySum(). Debugger användes stegvis och variabler inspekterades.

Lösning: Bytte stream-operator (rad 39 i BudgetService) där transaktionerna grupperas utifrån kategori från 
Transaction::category till transaction -> transaction.category().toLowerCase(). Detta resulterade i att 
den returnerade mapens keys enbart innehåller små bokstäver. Testet lyckades därefter. Syftet med denna ändring
är att säkerställa att kategorinamnen är lika oavsett om de är skrivna med stora eller små bokstäver. 

Problem:
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

Implementerade logger när main klassen byggdes. Eftersom det är i main som majoriteten av programmets 
inmatning från användaren sker så är det viktigt att logga användarinteraktioner och eventuella fel som 
uppkommer under användningen av programmet. Loggern hjälpte till att felsöka problem och förbättrade
användarvänligheten genom att ge insikter om hur programmet fungerar och vilka användarinteraktioner som utförs.
Där det gick byggdes programmet på ett sätt för att minimera behovet av felhantering som t.ex. vid menyvalen
där scanner.hasNextInt() används istället för try-catch med ParseInt(). Vidare vid inmatningen av amount
användes scanner.hasNextDouble() istället för try-catch med ParseDouble() för att minimera behovet av felhantering.

Den egna implementerade InvalidTransactionException-klassen används för att hantera fel som 
uppstår när användaren försöker lägga till en transaktion med ogiltiga värden (0 eller negativt amount). 
Detta hjälper till att förbättra användarvänligheten genom att ge användaren tydliga meddelanden om vad som är fel 
och hur de kan åtgärda det. En möjlig ändring är att istället lägga till en egen konstruktor i Transaction klassen
där man kontrollerar att amount är större än 0 annars kasta InvalidTransactionException. En transaktion hade alltså
inte då tillåtits att skapas vid fel amount-värde. Denna kontroll sker just nu i addTransaction-metoden 
i BudgetService-klassen, vilket innebär att ett transaktion-object med fel amount-value 'får' skapas, men tillåts inte
att läggas till i repositoryn senare istället.


