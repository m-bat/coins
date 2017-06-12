 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */
	.text
	.align	1
_string.3:
	.byte	67
	.byte	111
	.byte	109
	.byte	112
	.byte	97
	.byte	116
	.byte	105
	.byte	98
	.byte	108
	.byte	101
	.byte	32
	.byte	116
	.byte	121
	.byte	112
	.byte	101
	.byte	32
	.byte	116
	.byte	101
	.byte	115
	.byte	116
	.byte	10
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

	.comm	_cvi,4
	.comm	_vi,4
	.comm	_ci,4
	.comm	_cvui,4
	.comm	_cui,4
	.comm	_vui,4
