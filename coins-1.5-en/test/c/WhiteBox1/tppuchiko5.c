/* tppuchiko5.c */

int tester(int x){
  int a;
  a = 0;
  while (a < 100){
	if (a == x * x)
	   return 1;
  a++;
  }
  return 0;
}
main(){
  int a;	
  for (a=0;a<=10; a++)
    if (tester(a)) printf("%d\n",a);
    else printf("not %d\n",a); 
}
