#include<stdio.h>
/* InsertionSort.c */
int main(){
  int A[10];
    int j;
    int i;
    int key;

    A[0] = 5;
    A[1] = 3;
    A[2] = 7;
    A[3] = 2;
    A[4] = 9;
    A[5] = 4;
    A[6] = 11;
    A[7] = 6;
    A[8] = 38;
    A[9] = 20;

    j = 1;

    key = 0;
    i = 0;
    
    do{
			if(j < 10){
				key = A[j];
				i = j - 1;
				
				do{
					if(i >= 0 && key < A[i]){
						A[i + 1] = A[i];
						i = i - 1;
					}else{
						break;
					}
				}while(1);
				
				A[i + 1] = key;
				j = j + 1;
			}else{
				break;
			}
    }while(1);
        
		/* uncomment by FUKUOKA Takeaki */
    for(i = 0; i < 10; i++){
      printf("A[%d] = %d ",i,A[i]);
    }
    printf("\n");

    return(0);
}
