 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */
	.text
	.align	1
_string.11:
	.byte	32
	.byte	116
	.byte	112
	.byte	100
	.byte	101
	.byte	99
	.byte	108
	.byte	49
	.byte	58
	.byte	32
	.byte	108
	.byte	111
	.byte	99
	.byte	97
	.byte	108
	.byte	32
	.byte	100
	.byte	101
	.byte	99
	.byte	108
	.byte	97
	.byte	114
	.byte	97
	.byte	116
	.byte	105
	.byte	111
	.byte	110
	.byte	32
	.byte	111
	.byte	110
	.byte	108
	.byte	121
	.byte	32
	.byte	10
	.byte	0

	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$400,%esp
	pushl	$_string.11
	call	_printf
	leal	4(%esp),%esp
.L3:
	leave
	ret

