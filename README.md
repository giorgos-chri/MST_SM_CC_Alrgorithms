# MST_SM_CC_Alrgorithms

   Ant colony algorithms for the solution of the MST, Stable Marriage and Coin Change problems.
In this implementation of the algorithms we create an ant colony of black and red ants.

1. Each ants position is determined by (x,y) coordinates.
2. The black ants collect the food and the red ants carry it back tou the anthill.
3. The number of black ants equals to the red ants number.
4. Each black and can carry from 5 different seed species and from each seed can carry as much as it wants.

So the data has the following form:

Ant Type    Coordinates    Details

0(red)      0.313 0.322   1432(Capacity) <br />
1(black)    0.865 0.765   12 14 78 1 78(weight per seed) <br />
0(red)      0.465 0.54   654(Capacity) <br />
1(black)    0.657 0.329   12 14 78 1 147(weight per seed) <br />
...


