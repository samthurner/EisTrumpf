###### **=================================**

######              **Vorlage**

###### **=================================**

{

&nbsp; "deck\_name": "",              <-- **String** (z.B. "Fußballer")

&nbsp; "karten\_anzahl": ,            <-- **Integer**, muss mit Anzahl der cards übereinstimmen

&nbsp; "statistiken": \[              <-- **Genau 4 Einträge erforderlich**

&nbsp;   {

&nbsp;     "name": "",               <-- **String** (z.B. "Geschwindigkeit")

&nbsp;     "einheit": ""             <-- **String** (z.B. "km/h", darf auch leer sein "")

&nbsp;   },

&nbsp;   { "name": "", "einheit": "" },

&nbsp;   { "name": "", "einheit": "" },

&nbsp;   { "name": "", "einheit": "" }

&nbsp; ],

&nbsp; "cards": \[                    <-- Mindestens 2 Karten, *empfohlen*: 16

&nbsp;   {

&nbsp;     "name": "",               <-- **String** (z.B. "Messi")

&nbsp;     "statistik\_1": ,          <-- **Integer** (0 - 100)

&nbsp;     "statistik\_2": ,          <-- **Integer** (0 - 100)

&nbsp;     "statistik\_3": ,          <-- **Integer** (0 - 100)

&nbsp;     "statistik\_4":            <-- **Integer** (0 - 100), kein Komma am Ende!

&nbsp;   }

&nbsp; ]

}

