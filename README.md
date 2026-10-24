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
transaction -> transaction.category().equalsIgnoreCase(category). Testet lyckades därefter.