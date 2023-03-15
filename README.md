# BitOperationsCheckers

Warcaby, dla dwóch graczy - poruszających się za pomocą jednej klawiatury.
Całość odbywa się bez interfejsu graficznego, plansza jest rysowana w terminalu.
Gra prezentuje klasyczny zestaw zasad, jednak stan gry jest przechowywany w dwóch zmiennych typu long.

Bity przetrzymujące informacje funkcjonują wobec poniższych kryteriów:
  b2,b1,b0 - współrzędna X na planszy; 
  b5,b4,b3 - współrzędna Y na planszy; 
  b6 - kolor (0 - czarny, 1 - biały); 
  b7 - figura (0 - pion, 1 - damka); 
  b8 - stan (0 - zbity, 1 - w grze).
 
