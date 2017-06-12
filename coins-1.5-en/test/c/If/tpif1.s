 .ident "Coins Compiler version: coins-1.4.4.1 + BackEnd-1.0"
/* JavaCG for target:sparc convention:standard */
	.section ".text"
	.align	1
string.7:
	.byte	99
	.byte	61
	.byte	37
	.byte	100
	.byte	10
	.byte	0

	.align	4
	.global	main
main:
	save	%sp,-96,%sp
	mov	1,%i1
	mov	2,%i0
	add	%i1,2,%i0
	sethi	%hi(string.7),%o0
	or	%o0,%lo(string.7),%o0
	mov	%i0,%o1
	call	printf
	nop
	ret
	restore

