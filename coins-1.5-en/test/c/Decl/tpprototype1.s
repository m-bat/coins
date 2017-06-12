 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */

	.text
	.align	4
	.global	_point2fault
_point2fault:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$8,%esp
	movl	8(%ebp),%eax
	fldz
	fstpl	-8(%ebp)
	fldl	-8(%ebp)
	leave
	ret

	.align	1
_string.7:
	.byte	100
	.byte	61
	.byte	37
	.byte	102
	.byte	32
	.byte	10
	.byte	0

	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$88,%esp
	leal	-80(%ebp),%eax
	pushl	%eax
	call	_point2fault
	fstpl	-88(%ebp)
	leal	4(%esp),%esp
	pushl	4-88(%ebp)
	pushl	-88(%ebp)
	pushl	$_string.7
	call	_printf
	leal	12(%esp),%esp
	movl	$0,%eax
	leave
	ret

