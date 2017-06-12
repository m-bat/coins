 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */

	.text
	.align	4
	.global	_func
_func:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$56,%esp
	movl	8(%ebp),%ecx
	movl	12(%ebp),%eax
	flds	(%ecx)
	fadds	(%eax)
	fstps	-16(%ebp)
	flds	-16(%ebp)
	leave
	ret

	.align	1
_string.16:
	.byte	114
	.byte	101
	.byte	115
	.byte	117
	.byte	108
	.byte	116
	.byte	32
	.byte	61
	.byte	32
	.byte	37
	.byte	53
	.byte	46
	.byte	53
	.byte	102
	.byte	10
	.byte	0

	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$112,%esp
	flds	.LC1
	fstps	-4(%ebp)
	flds	.LC2
	fstps	-8(%ebp)
	flds	-4(%ebp)
	fadds	-8(%ebp)
	sub	$8,%esp
	fstpl	(%esp)
	pushl	$_string.16
	call	_printf
	leal	12(%esp),%esp
	movl	$0,%eax
	leave
	ret

	.align	4
.LC1:
	.long	0x425551ec /* 53.33000183105469 */
	.align	4
.LC2:
	.long	0x4587c379 /* 4344.43408203125 */
