#include<stdio.h>
/* lost.c */
int main(){
   int x;
   int hoge;
   
   x = 1;
   hoge = 3;
   
   do{
      hoge = x;
      x = 2;
			/*
   }while(1);
	 by FUKUOKA Takeaki
			*/
	 }while(x<0);
   printf("%d,%d\n",x,hoge); /* Added by M.Takahashi */
   return hoge;
}
