/* tpinit2-1.c  Initial value for array (Decl) */
/*    Fukuda mail 031013 */

int printf(char*, ...);

int main(){
  int a[]={1,2,3};
  int b[]={4,5,6};
  printf("{%d,%d,%d}\n",a[0],a[1],a[2]);
  printf("{%d,%d,%d}\n",b[0],b[1],b[2]);
  return 0;
}

