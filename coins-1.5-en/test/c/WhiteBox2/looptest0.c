/* looptest0.c */
/*   isSimpleWhileLoop  Hasegawa mail 040128 */

int main()
{
  int i = 0;
  int s = 0;
  while (0) 
     ; 
  while (i) 
     ; 
  while (i <= 10) {
    s = s + i;
    i = i + 1;
  }
  printf(" %d \n", i);
  for (i = 0; i<10; i++) {
  }
  do { i++; }
  while (i< 20);  
  printf(" %d \n", i);
}
