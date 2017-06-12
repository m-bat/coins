/* tptaru1.c */

int main()
{
 int i;
 i = 3;
 if(i) {
   printf("then: i=%d\n",i); /* SF030620 */
   return i+1;
 } else {
   printf("else: i=%d\n",i); /* SF030620 */
   return i+2;
 }
 printf("i=%d\n",i); /* SF030620 */
 return i;
}
