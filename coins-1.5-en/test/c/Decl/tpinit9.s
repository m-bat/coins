 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */
	.text
	.align	1
_string.5:
	.byte	42
	.byte	98
	.byte	91
	.byte	48
	.byte	93
	.byte	61
	.byte	37
	.byte	100
	.byte	32
	.byte	10
	.byte	0

	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$8,%esp
	leal	-4(%ebp),%eax
	movl	%eax,-8(%ebp)
	movl	$3,-4(%ebp)
	movl	-8(%ebp),%eax
	pushl	(%eax)
	pushl	$_string.5
	call	_printf
	leal	8(%esp),%esp
	movl	$0,%eax
	leave
	ret

