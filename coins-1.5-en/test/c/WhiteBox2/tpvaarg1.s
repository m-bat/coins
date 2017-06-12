 .ident "Coins Compiler version: coins-1.4.4.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */

	.text
	.align	4
	.global	_sum
_sum:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	movl	8(%ebp),%ecx
	movl	$0,%eax
	leal	12(%ebp),%ebx
.L4:
	movl	%ecx,%edx
	leal	-1(%ecx),%ecx
	cmpl	$0,%edx
	je	.L7
.L6:
	leal	4(%ebx),%ebx
	addl	-4(%ebx),%eax
	jmp	.L4
.L7:
	popl	%ebx
	leave
	ret

	.align	1
_string.8:
	.byte	37
	.byte	100
	.byte	10
	.byte	0

	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	$321
	pushl	$222
	pushl	$123
	pushl	$3
	call	_sum
	leal	16(%esp),%esp
	pushl	%eax
	pushl	$_string.8
	call	_printf
	leal	8(%esp),%esp
.L11:
	leave
	ret

