 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */
	.text
	.align	1
_string.5:
	.byte	37
	.byte	100
	.byte	10
	.byte	0

	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$12,%esp
	movl	$123,-12(%ebp)
	movl	$456,-8(%ebp)
	movl	$789,-4(%ebp)
	movl	$_c,%ecx
	movl	$_b,_c+12
	leal	-12(%ebp),%eax
	movl	%eax,_b+8
	movl	12(%ecx),%eax
	movl	8(%eax),%eax
	pushl	4(%eax)
	pushl	$_string.5
	call	_printf
	leal	8(%esp),%esp
	movl	$0,%eax
	leave
	ret

	.comm	_c,28
	.comm	_b,20
