/* tpcall7.c  -- Parameter discrepancy test */

/* int func1(int p); SF030609 */
int func1(int p){}; /* SF030609 */

main()
{
  func1(1);
  printf("Parameter discrepancy test\n"); /* SF030609 */
}
