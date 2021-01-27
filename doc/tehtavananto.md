# Käyttöliittymät 2021 - Harjoitus D

Kurssin viimeinen harjoitus on itsenäinen ohjelmointitehtävä. Tarkoituksena on soveltaa kurssilla
jo ohjelmointiesimerkein läpikäytyjä tekniikoita luovasti ja itsenäisesti ilman tarkentavia
apukysymyksiä ja vihjeitä.

Työn aihe lienee kaikille varmasti tuttu: perinteinen kahden pelaajan laivanupotuspeli. Lisätietoa
pelistä löytyy esimerkiksi Wikipediasta
[https://fi.wikipedia.org/wiki/Laivanupotus](https://fi.wikipedia.org/wiki/Laivanupotus).
Peliprojektin mukana tarjotaan valmiina joitain vapaasti lisensoituja kuvia, joita voi
halutessaan käyttää. Peliä saa soveltaa tehtävässä vapaasti annettujen ehtojen rajoissa. Pelin voi myös
teemoittaa ihan eri ympäristöön kuin merelle, esim. avaruuteen ja korvata alukset Star Trek
-tyylisillä avaruusaluksilla ja käyttöliittymän [LCARS-teemalla](https://en.wikipedia.org/wiki/LCARS).

## Ohjelmointikieli ja GUI-tekniikka

Työn voi toteuttaa JavaFX:llä viikkoharjoituksissa opitun pohjalta, selvitystyön (harjoitus B) tekniikalla tai jollain muulla ryhmän haluamalla tekniikalla. Tekniikkaa valitessa kannattaa kuitenkin muistaa, että valitettavasti emme pysty tarjoamaan ohjausta tai muuta apua kaikkiin erilaisiin ympäristöihin. 

Tuetuita tekniikoita ovat mm:
* Java / JavaFX
* C++ / Qt
* HTML5 / CSS / Javascript
* React

Jos suunnittelette jonkin muun tekniikan käyttöä, varmistakaa etukäteen sen sopivuus.


## Ohjelman suorituksen kulku

![Vuokaavio](/doc/ohjelmankulku_FI.png)

Ohjelman käynnistymisen jälkeen ensimmäisessä näkymässä kysytään pelaajien nimet ja pelin asetukset. Pelaajat voivat päättää peliruudukon koon ja alusten määrän. Ohjelman tulee varmistaa, että aluksia ei ole liikaa ruudukon kokoon nähden eli ne kaikki mahtuvat pelialueelle. 

Kun asetukset ovat paikoilleen, pelaajat asettavat aluksensa pelialueelle. Peliä pelataan vain yhdellä koneella/ohjelmalla, joten toisen pelaajan tulee katsoa muualle, kun ei ole hänen vuoronsa. Ryhmä voi halutessaan tehdä myös ohjelman verkkoversiona, jolloin edellä mainittua ongelmaa ei ole. Tätä ei kuitenkaan vaadita. 

Varsinainen peli käynnistyy, kun pelaaja 2 on saanut aluksensa paikoilleen. Pelaajat ampuvat vuorotellen. Jos vuorossa oleva pelaaja osuu, hän saa ampua uudelleen. Vuorojen välissä näytetään väliruutu, jotta pelaajat voivat vaihtaa paikkoja helposti ilman toisen pelaajan pelikentän näkemistä.

Jos ammus ei osu alukseen, merkitään hutilaukaus sen ampuneen pelaajan ruudukkoon. Osuma merkitään molempien pelaajien ruudukkoon kuten myös aluksen uppoaminen. 

Pelikierros jatkuu kunnes jommankumman pelaajan kaikki alukset ovat uponneet. Tämän jälkeen ohjelma kysyy, halutaanko pelata uusi kierros. 

Huomaa, että ohjelman täytyy muistaa sekä pelin asetukset että pelitilanne näkymien vaihtuessa. Vasta uuden pelikierroksen käynnistyessä tilanne nollataan.


## Minimivaatimukset

### Peliruudun koko

Peliruudukon koko vaihtelee käyttäjän asetuksen mukaan. Ruudukon sallittu minimikoko on 5 x 5 ruutua. Sallitun maksimikoon tulee olla 10 x 10 ruutua. Ryhmä voi halutessaan tukea isompiakin ruudukoita.

### Alusten alustyypit, minimimäärät ja koot

Pelissä on 5 erilaista alustyyppiä. Niiden määrät vaihtelevat pelaajien valinnan mukaan. Minkä tahansa alustyypin määräksi voidaan asettaa myös nolla, mutta peli voi alkaa vasta kun pelattavana on vähintään yksi alus.

Alla olevat alustyypit ja -määrät tulee kyetä lisäämään 10x10 ruudun pelialueelle.

| Määrä | Alus          | Koko ruutuina |
| :---: | ---           | :---: |
| 1     | lentotukialus | 5     |
| 2     | taistelulaiva | 4     |
| 3     | risteilijä    | 3     |
| 4     | sukellusvene  | 3     |
| 5     | hävittäjä     | 2     |

Käyttöliittymä tarkistaa sallitun alusten määrän siten, että ruudukon pinta-ala (RA) tulee olla kaksinkertaisesti alusten yhteenlaskettu pinta-ala (AA). Esimerkiksi edellä 10x10-ruudukon pinta-ala `RA = 10 * 10=100` ja alusten `AA = 1*5 + 2*4 + 3*3 + 4*3 + 5*2 = 44`. Ja näin saadaan `100 >= 2 * 44` eli `RA >= 2 * AA` eli asetukset ovat sallitut.

### Toiminnalliset vaatimukset

* Pelin aloittamisen ja lopettamisen valinnat (peli halutaan lopettaa koska tahansa)
* Pelin alussa kysytään pelaajien nimet
* Nimien jälkeen kysytään peliruudukon koko
* Peliruudukon koon jälkeen kysytään alusten määrä ja verrataan arvon sopivuus ruudukon kokoon (`RA >= 2*AA`)
 (Matemaattisesti voidaan em. alan summasäännöstä todistaa, että aluksen mahtuvat ruudukkoon aina jossain asetelmassa)
  * Mikäli pelaaja ei onnistu asettamaan kaikkia aluksia ruudukkoon, asettamisen voi aloittaa alusta.
* Alukset asetetaan paikoilleen vedä ja pudota -menetelmällä (drag'n'drop)
* Alusta asetettaessa sitä voi pyörittää näppäimistön `r`-näppäimellä
  * ryhmä voi halutessaan myös toteuttaa alusten lisäämisen nuolinäppäimillä ja Enterillä
* Ampuminen tapahtuu klikkaamalla hiirellä haluttua kohderuutua
  * ryhmä voi halutessaan myös toteuttaa ampumisen nuolinäppäimillä ja Enterillä
* Alukset tulee esittää joko kuvina tai geometrisina kuvioina
* Dynaamisesti valitun ruudukon koon mukaisesti piirtyvä pelialue
  (grafiikkaa ei tule lukea yhtenä kuvana tiedostosta)

### Käytettävyys

* Sovella ohjelman toteutukseen vähintään jokaisessa listatussa toiminnallisessa vaatimuksessa hyvän
  käytettävyyden periaatteita ja suunnittelumalleja

### Dokumentointi

* Dokumentoi lyhyesti koodin kommenteiksi luokka/metoditasolla, mitä mikäkin osa
  ratkaisussasi tekee.
* Lisäksi dokumentoi ohjelman rakennus ja käyttö, mikäli käytät
  muita tekniikoita kuin Java / JavaFX / Maven.


## Arviointikriteerit

Työt arvioidaan asteikolla 0--24 (0 = hylätty / ei palautettu).
Arviointi perustuu seuraaviin osa-alueisiin.
Oletusarvoisesti kaikki ryhmän jäsenet saavat saman arvosanan ellei
ryhmä toivo toisin.
Työn palautuksen deadline eli viimeinen palautuspäivä on 14.3.

### Ryhmän kontribuutiot

Kirjaa lyhyeksi raportiksi esim. README-tiedostoon ryhmän jäsenten
nimet ja opiskelijanumerot, sekä kunkin ryhmän jäsenen
kontribuutio työssä.


### 1. Vaatimusten täyttyminen

* Täyttyvätkö tehtävänannon minimivaatimukset?
* Onko dokumentointi riittävä ja ymmärrettävä?


### 2. Käytettävyys
* onko ohjelma helposti opittava (esim. millaisia vihjeitä se antaa käyttäjälle, kuinka johdonmukainen se on)?
* onko ohjelma tehokas käyttää (esim. onko navigointi toimintojen välillä helppoa)?
* onko ohjelman näkyvyys hyvä (onko sen kulloinenkin sisäinen tila käyttäjälle helposti ymmärrettävä, onko selvää miten päästään seuraavaan toimintoon tai haluttuun päämäärään)?
* antaako ohjelma käyttäjälle tarpeeksi informatiivista palautetta?
* virheet: onko ohjelma käyttäjän virheiden suhteen anteeksiantava?
* tyytyväisyys: onko ohjelma helppo ja vaivaton käyttää?
		

### 3. Käyttäjäkokemus
* Onko suunnittelumalleja käytetty?
* Onko ohjelma visuaalisesti/esteettisesti miellyttävä?
* Huomioidaanko jotenkin erilaisia käyttäjäryhmiä? 
* Onko peliä hauska pelata?

## Ominaisuudet, joita ei tarvitse toteuttaa

Seuraavat sivuutetaan arvioinnissa

* Tietokonepelaaja ja sille tekoäly
* Verkkoliikenne (jos teet web-tekniikoilla, puhtaasti selainpohjainen SPA riittää!)
* 3D-grafiikka

Seuraavat huomioidaan arvioinnissa kuitenkin bonuspisteinä

* Äänitehosteiden ohjelmointi
* Animaatiot
* Käyttöliittymän komponenttien koristelu tekstuureilla ja omalla teemalla
* Koko ohjelman ohjaus eri tilanteissa näppäimistöllä hiiren lisäksi
