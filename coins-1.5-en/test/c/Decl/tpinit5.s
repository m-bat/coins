 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */
	.text
	.align	1
_string.4:
	.byte	97
	.byte	61
	.byte	37
	.byte	100
	.byte	32
	.byte	60
	.byte	60
	.byte	32
	.byte	73
	.byte	110
	.byte	105
	.byte	116
	.byte	105
	.byte	97
	.byte	108
	.byte	32
	.byte	118
	.byte	97
	.byte	108
	.byte	117
	.byte	101
	.byte	32
	.byte	32
	.byte	40
	.byte	68
	.byte	101
	.byte	99
	.byte	108
	.byte	41
	.byte	10
	.byte	0

	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	movl	$1,%eax
	movl	$1,%eax
	pushl	$1
	pushl	$_string.4
	call	_printf
	leal	8(%esp),%esp
.L3:
	leave
	ret

