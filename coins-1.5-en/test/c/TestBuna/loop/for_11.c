/* for_11 simple for statement */
/* endless loop */
int printf(char *s, ...);

int main() {
        int i=0;
        for ( ; ; ){   /* endless loop */
          if (i>10) break;
          i++; 
        }
         printf("i = %d\n", i);
	return 0;
}