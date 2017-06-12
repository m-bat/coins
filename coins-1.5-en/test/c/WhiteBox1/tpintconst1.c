/* tpintconst1.c Integer constants */ 
/* (Decl) */

main()
{
  int i, j, k, sumx;
  unsigned int sumu;
   
  sumx = 0;
  i = 32767;
  j = i + 32768;
  k = 2147483647;
  sumx = -2147483648 + k + i; 
  sumu =  2147483648 - k + i; 
  printf("%d %u %d \n", sumx, sumu, k);
  return 0;
}

