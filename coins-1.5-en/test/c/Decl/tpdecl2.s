 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */
	.text
	.align	1
_string.3:
	.byte	32
	.byte	116
	.byte	112
	.byte	100
	.byte	101
	.byte	99
	.byte	108
	.byte	50
	.byte	58
	.byte	32
	.byte	103
	.byte	108
	.byte	111
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
	.byte	119
	.byte	105
	.byte	116
	.byte	104
	.byte	32
	.byte	101
	.byte	109
	.byte	112
	.byte	116
	.byte	121
	.byte	32
	.byte	109
	.byte	97
	.byte	105
	.byte	110
	.byte	32
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
.L3:
	leave
	ret

	.comm	_n,4
	.comm	_p,4
	.comm	_l,4
	.comm	_k,4
	.comm	_o,4
	.comm	_j,400
	.comm	_i,4
	.comm	_m,4
