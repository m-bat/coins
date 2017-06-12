 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */
	.text
	.align	1
_string.3:
	.byte	100
	.byte	101
	.byte	102
	.byte	97
	.byte	117
	.byte	108
	.byte	116
	.byte	32
	.byte	116
	.byte	121
	.byte	112
	.byte	101
	.byte	0

	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	$_string.3
	call	_printf
	leal	4(%esp),%esp
	movl	$0,%eax
	leave
	ret

