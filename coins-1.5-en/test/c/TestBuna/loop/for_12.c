/* for_12 simple for statement */
/* endless loop */
int printf(char *s, ...);

int main() {
        int i;
        for (i=0 ;  ; i++ ){   /* endless loop */
          if (i>10) break;
        }
         printf("i = %d\n", i);
	return 0;
}