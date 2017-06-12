 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */
	.text
	.align	1
_string.7:
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
	movl	$_x.1,-12(%ebp)
	movl	$_y.2,-8(%ebp)
	movl	$_z.3,-4(%ebp)
	movl	-8(%ebp),%eax
	pushl	(%eax)
	pushl	$_string.7
	call	_printf
	leal	8(%esp),%esp
	movl	$0,%eax
	leave
	ret

	.data
	.align	4
_x.1:
	.long	123
	.align	4
_y.2:
	.long	456
	.align	4
_z.3:
	.long	789
