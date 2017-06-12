 .ident "Coins Compiler version: coins-1.4.4.1 + BackEnd-1.0"
/* JavaCG for target:sparc convention:standard */
	.section ".text"
	.align	1
string.12:
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
	mov	1,%i0
	mov	2,%i2
	cmp	%i2,0
	ble	.L9
	nop
.L8:
	add	%i0,2,%i1
.L9:
	cmp	%i0,0
	bne	.L14
	nop
.L10:
	cmp	%i2,0
	bne	.L12
	nop
.L11:
	mov	%i0,%i1
	mov	3,%i0
	ba	.L13
	nop
.L12:
	mov	1,%i1
	mov	4,%i2
.L13:
	add	%i0,%i2,%i0
	add	%i0,%i1,%i0
	ba	.L17
	nop
.L14:
	cmp	%i2,0
	ble	.L16
	nop
.L15:
	add	%i0,2,%i0
.L16:
	mov	5,%i0
.L17:
	add	%i0,1,%i1
	sub	%i1,%i0,%i1
	sub	%i1,1,%o1
	sethi	%hi(string.12),%o0
	or	%o0,%lo(string.12),%o0
	call	printf
	nop
	ret
	restore

