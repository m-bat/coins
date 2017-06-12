 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */

	.text
	.align	4
	.global	_f
_f:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$800,%esp
	movl	8(%ebp),%ecx
	movl	$15,-400(%ebp)
	movl	$0,-396(%ebp)
	movl	$3,-392(%ebp)
	movl	$3,%eax
.L3:
	cmpl	$100,%eax
	jge	.L5
.L4:
	movl	$0,-400(%ebp,%eax,4)
	leal	1(%eax),%eax
	jmp	.L3
.L5:
	movb	$32,-800(%ebp)
	movb	$99,-799(%ebp)
	movb	$100,-798(%ebp)
	movl	$3,%eax
.L6:
	cmpl	$400,%eax
	jge	.L8
.L7:
	movb	$0,-800(%ebp,%eax)
	leal	1(%eax),%eax
	jmp	.L6
.L8:
	movsbl	-800(%ebp,%ecx),%eax
	addl	-400(%ebp,%ecx,4),%eax
	leave
	ret

	.align	1
_string.15:
	.byte	37
	.byte	100
	.byte	32
	.byte	37
	.byte	100
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
	subl	$800,%esp
	pushl	%ebx
	pushl	%esi
	movl	$5,-800(%ebp)
	movl	$6,-796(%ebp)
	movl	$0,-792(%ebp)
	movl	$3,%eax
.L12:
	cmpl	$200,%eax
	jge	.L14
.L13:
	movl	$0,-800(%ebp,%eax,4)
	leal	1(%eax),%eax
	jmp	.L12
.L14:
	movl	$0,%esi
	movl	$0,%ebx
	movl	$0,%eax
.L15:
	cmpl	$100,%eax
	jge	.L17
.L16:
	addl	_x(,%eax,4),%esi
	addl	-800(%ebp,%eax,4),%ebx
	leal	1(%eax),%eax
	jmp	.L15
.L17:
	pushl	$2
	call	_f
	leal	4(%esp),%esp
	pushl	%ebx
	pushl	%esi
	pushl	%eax
	pushl	$_string.15
	call	_printf
	leal	16(%esp),%esp
	movl	$0,%eax
	popl	%esi
	popl	%ebx
	leave
	ret

	.data
	.align	4
	.global	_x
_x:
	.long	10
	.long	11
	.long	12
	.long	1
	.skip	384
	.align	1
	.global	_c1
_c1:
	.byte	32
	.byte	97
	.byte	98
	.skip	397
