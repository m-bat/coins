/* tpcallcondSwitch2.c  Call function with loop and switch and label 
   in conditional exp */

int printf(char*, ...);

int tester(int x){
  int i, a, b;
  a = 0;
  b = a+1;
  if (a > 10)
    goto lab8;
  while (a < 100){
	if (a == x * x)
	   return 1;
    a++;
  }
  switch (b) {
  case 0:
    a = 0;
    break;
  case 1:
    b = 1;
  case 2:
    a = b+2;
  case 3:
    a = 0;
    break;
  case 4:
    b = 1;
  case 5:
    a = b+2;
    break;
  default:
    a = 3;
    break;
  };
lab8:
  return 0;
}
main(){
  int a;	
  a = 1;
  if (a > 10)
    goto lab8;
  for (a=0;a<=10; a++)
    if (tester(a)) printf("%d\n",a);
    else printf("not %d\n",a); 
  lab8: ; /* ##82 */
}
