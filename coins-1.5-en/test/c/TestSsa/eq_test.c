#include<stdio.h>
int main(){
  
  int a,b;
  a=1;
  b=1;
  
  do{
    if(a>2){
      a=a+1;
      b=b+1;
    }
    else{
      a=a+2;
      b=b+2;
    }
    
  }while(a<14);
  printf("%d,%d\n",a,b);   /* Added by M.Takahashi */ 
  return(2);
}
