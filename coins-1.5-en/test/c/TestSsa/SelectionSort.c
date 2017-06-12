#include<stdio.h>
/* SelectionSort.c */
int main(){
    int n;
    
    int A[10];
    int index;
    int i;
    int j;
    int min;
		

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

		
    index = 0;
    i = 0;
    j = 0;
    min = 0;
    n = 10;
		
    do{
			if(i < n - 1){
				index = i;	   
				min = A[index];
				j = i + 1;
				
				do{
					if(j < n){
						if(min > A[j]){
							index = j;
							min = A[index];
						}
					}else{
						break;
					}
					j = j + 1;
				}while(1);
				
				A[index] = A[i];
				A[i] = min;
				
			}else{
				break;
			}
			i = i + 1;
    }while(1);    
		
    
    for(i = 0; i < 10; i++){
      printf("A[%d] = %d \n",i,A[i]);
    }
    printf("\n");
    
		
    return 0;
}
