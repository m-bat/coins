/*--------------------------------------------------------*/
#include <stdio.h>
#include <string.h>

void scandate();
int main()
{
  scandate();
	return 0;
}
/*--------------------------------------------------------*/
void scandate()
{
	char line[1000];
	int getline(char *, int linesize);
	int year, month, day;
	char monthname[7];

	while (getline(line, sizeof(line)) > 0) {
	if (sscanf(line, "%d %s %d", &day, monthname, &year) == 3)
		printf("valid: %s\n", line);     /* 25 Dec 1988 */
	else if (sscanf(line, "%d/%d/%d", &month, &day, &year) == 3)
		printf("valid: %s\n", line);     /* mm/dd/yy */
	else
		printf("invalid: %s\n", line);
	}
}
/*--------------------------------------------------------*/

int getline(char * pLine, int pLineSize)
{
  /* static nth = 0; */
  static int nth = 0;
  nth++;
  switch (nth) {
  case 1:
    strcpy(pLine, "25 Dec 1988");
    return 1;
  case 2:
    strcpy(pLine, "1 Jan 1989");
    return 2;
  default:
    return 0;
  }
}
