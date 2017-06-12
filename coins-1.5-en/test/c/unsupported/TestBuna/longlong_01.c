int main() {
	long long sll, result;
	int si;
	sll = 1;
	si = 12345; /* SF030514 */
	result = sll + si;
	printf("si = %d\n",si); /* SF030514 */
	printf("sll = %lld\n",sll); /* SF030514 */
	printf("result = %lld\n",result); /* SF030514 */
}
