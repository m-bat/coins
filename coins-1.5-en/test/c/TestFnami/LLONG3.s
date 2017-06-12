 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */
	.text
	.align	1
_string.3:
	.byte	37
	.byte	120
	.byte	10
	.byte	0

	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	movl	$305419896,%eax
	movl	$0,%edx
	movl	%eax,_a
	movl	%edx,_a+4
	movl	_a,%eax
	movl	_a+4,%edx
	movl	%eax,%eax
	pushl	%eax
	pushl	$_string.3
	call	_printf
	leal	8(%esp),%esp
	movl	$0,%eax
	leave
	ret

	.comm	_a,8
