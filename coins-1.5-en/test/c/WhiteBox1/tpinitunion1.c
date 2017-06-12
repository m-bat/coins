/* tpinitunion1.c  Initial value for union */
/* Yamano mail 021206 */

typedef union {
  int x, x2;
  double y;
} sharedData;

sharedData data1 = { 101 }; 
sharedData data2 = { 102 };
sharedData data3 = { 3.0 };

int main()
{
  printf("%d %f %d %d \n", data1.x, data2.y, data2.x2, data3.x2);
}

