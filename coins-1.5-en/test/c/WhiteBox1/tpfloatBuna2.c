/* tpfloatBuna2.c: float parameter test. */
/*   Yamano mail 030512 */

 #include <stdio.h>
 
void func(int, float);
 
int main() {
   float f = 1.0F;
   func(1, f);
 }
 
void func(int i, float f) {
   printf("i=%d, f=%f\n", i, f);
 }
