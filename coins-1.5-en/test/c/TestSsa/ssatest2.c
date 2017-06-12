#include<stdio.h>
int g;

int main(){
  int a,b,z,y,x;
  
  a=1;
  b=2*a;
  z=1*b+a;
	
  x=0;
  g=0;

  if(a>1){
    /*int z=10; */
    y=z;
    
    if(y>0){
      z=y;
      y=10;
    }
    else{
      z=y;
      y=0;
      x=y+1;
      y=x*z;
    }
    
    b=z;
  }
  else{
    y=z+a+b;
  }
  a=y;
  /*
    switch(a){
    case 0:a=0;break;
    case 1:a=1;break;
    case 2:a=2;break;
    case 3:a=3;break;
    default:a=10;
    }
  */
  printf("%d\n",a);
  return(0);
}
