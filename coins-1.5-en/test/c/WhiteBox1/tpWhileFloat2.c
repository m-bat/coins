/* tpWhileFloat1.c a211rev2.c 020327 (Modified by Tan) */

main()
{
  int i = 0;
  while ((0.1 <= 0.3)&&(i < 3)) {
    printf("in while loop %d \n", i);
    i = i + 1;
  }
}

