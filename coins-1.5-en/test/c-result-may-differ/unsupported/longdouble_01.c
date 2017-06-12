int main() {
	long double ld, result;
	double d;
	ld = 1.1;
	d = 0.1; /* SF030514 */
	result = ld - d;
	printf("d = %f\n",d); /* SF030514 */
	printf("ld = %Lf\n",ld); /* SF030514 */
	printf("result = %Lf\n",result);
}