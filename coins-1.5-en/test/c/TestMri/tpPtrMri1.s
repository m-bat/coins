 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */
	.text
	.align	1
_string.8:
	.byte	42
	.byte	99
	.byte	112
	.byte	105
	.byte	32
	.byte	37
	.byte	100
	.byte	32
	.byte	42
	.byte	112
	.byte	99
	.byte	105
	.byte	32
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
	subl	$4,%esp
	leal	-4(%ebp),%eax
	movl	$3,%eax
	movl	_cpi,%eax
	movl	$1,(%eax)
	movl	$_ci,_pci
	movl	_pci,%eax
	pushl	(%eax)
	movl	_cpi,%eax
	pushl	(%eax)
	pushl	$_string.8
	call	_printf
	leal	12(%esp),%esp
.L3:
	leave
	ret

	.data
	.align	4
	.global	_cpi
_cpi:
	.long	_i
	.align	4
	.global	_ci
_ci:
	.long	3
	.comm	_pci,4
	.comm	_pi,4
	.comm	_i,4
	.comm	_j,4
