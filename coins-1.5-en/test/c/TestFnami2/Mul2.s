 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */

	.text
	.align	4
	.global	_f0
_f0:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%eax
	movl	%eax,%ecx
	imull	$-2147483647,%ecx
	pushl	%ecx
	movl	%eax,%ecx
	imull	$214748364,%ecx
	pushl	%ecx
	movl	%eax,%ecx
	imull	$171,%ecx
	pushl	%ecx
	movl	%eax,%ecx
	imull	$86,%ecx
	pushl	%ecx
	movl	%eax,%ecx
	imull	$6,%ecx
	pushl	%ecx
	movl	%eax,%ecx
	imull	$5,%ecx
	pushl	%ecx
	leal	(,%eax,4),%ecx
	pushl	%ecx
	movl	%eax,%ecx
	imull	$3,%ecx
	pushl	%ecx
	leal	(,%eax,2),%ecx
	pushl	%ecx
	pushl	%eax
	pushl	$0
	pushl	$_s11
	call	_printf
	leal	48(%esp),%esp
.L3:
	leave
	ret


	.align	4
	.global	_f1
_f1:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%eax
	movl	%eax,%ecx
	imull	$-2147483647,%ecx
	pushl	%ecx
	movl	%eax,%ecx
	imull	$214748364,%ecx
	pushl	%ecx
	movl	%eax,%ecx
	imull	$171,%ecx
	pushl	%ecx
	movl	%eax,%ecx
	imull	$86,%ecx
	pushl	%ecx
	movl	%eax,%ecx
	imull	$6,%ecx
	pushl	%ecx
	movl	%eax,%ecx
	imull	$5,%ecx
	pushl	%ecx
	leal	(,%eax,4),%ecx
	pushl	%ecx
	movl	%eax,%ecx
	imull	$3,%ecx
	pushl	%ecx
	leal	(,%eax,2),%ecx
	pushl	%ecx
	pushl	%eax
	pushl	$0
	pushl	$_s11
	call	_printf
	leal	48(%esp),%esp
.L6:
	leave
	ret


	.align	4
	.global	_op
_op:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%eax
	movl	12(%ebp),%ecx
	imull	%ecx,%eax
	leave
	ret


	.align	4
	.global	_f2
_f2:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$32,%esp
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	movl	8(%ebp),%eax
	movl	%eax,-8(%ebp)
	pushl	$0
	pushl	-8(%ebp)
	call	_op
	movl	%eax,-32(%ebp)
	leal	8(%esp),%esp
	pushl	$1
	pushl	-8(%ebp)
	call	_op
	movl	%eax,-28(%ebp)
	leal	8(%esp),%esp
	pushl	$2
	pushl	-8(%ebp)
	call	_op
	movl	%eax,-24(%ebp)
	leal	8(%esp),%esp
	pushl	$3
	pushl	-8(%ebp)
	call	_op
	movl	%eax,-20(%ebp)
	leal	8(%esp),%esp
	pushl	$4
	pushl	-8(%ebp)
	call	_op
	movl	%eax,-16(%ebp)
	leal	8(%esp),%esp
	pushl	$5
	pushl	-8(%ebp)
	call	_op
	movl	%eax,-12(%ebp)
	leal	8(%esp),%esp
	pushl	$6
	pushl	-8(%ebp)
	call	_op
	movl	%eax,-4(%ebp)
	leal	8(%esp),%esp
	pushl	$86
	pushl	-8(%ebp)
	call	_op
	movl	%eax,%edi
	leal	8(%esp),%esp
	pushl	$171
	pushl	-8(%ebp)
	call	_op
	movl	%eax,%esi
	leal	8(%esp),%esp
	pushl	$214748364
	pushl	-8(%ebp)
	call	_op
	movl	%eax,%ebx
	leal	8(%esp),%esp
	pushl	$-2147483647
	pushl	-8(%ebp)
	call	_op
	leal	8(%esp),%esp
	pushl	%eax
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	pushl	-4(%ebp)
	pushl	-12(%ebp)
	pushl	-16(%ebp)
	pushl	-20(%ebp)
	pushl	-24(%ebp)
	pushl	-28(%ebp)
	pushl	-32(%ebp)
	pushl	$_s11
	call	_printf
	leal	48(%esp),%esp
.L12:
	popl	%edi
	popl	%esi
	popl	%ebx
	leave
	ret


	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	_aa
	pushl	_a9
	pushl	_a8
	pushl	_a7
	pushl	_a6
	pushl	_a5
	pushl	_a4
	pushl	_a3
	pushl	_a2
	pushl	_a1
	pushl	_a0
	pushl	$_s11
	call	_printf
	leal	48(%esp),%esp
	pushl	$10
	pushl	$2147483640
	pushl	$1710
	pushl	$860
	pushl	$60
	pushl	$50
	pushl	$40
	pushl	$30
	pushl	$20
	pushl	$10
	pushl	$0
	pushl	$_s11
	call	_printf
	leal	48(%esp),%esp
	movl	$10,%eax
	pushl	$10
	pushl	$2147483640
	pushl	$1710
	pushl	$860
	pushl	$60
	pushl	$50
	pushl	$40
	pushl	$30
	pushl	$20
	pushl	$10
	pushl	$0
	pushl	$_s11
	call	_printf
	leal	48(%esp),%esp
	pushl	$10
	pushl	$2147483640
	pushl	$1710
	pushl	$860
	pushl	$60
	pushl	$50
	pushl	$40
	pushl	$30
	pushl	$20
	pushl	$10
	pushl	$0
	pushl	$_s11
	call	_printf
	leal	48(%esp),%esp
	movl	$10,%eax
	pushl	$10
	pushl	$2147483640
	pushl	$1710
	pushl	$860
	pushl	$60
	pushl	$50
	pushl	$40
	pushl	$30
	pushl	$20
	pushl	$10
	pushl	$0
	pushl	$_s11
	call	_printf
	leal	48(%esp),%esp
	pushl	$10
	pushl	$2147483640
	pushl	$1710
	pushl	$860
	pushl	$60
	pushl	$50
	pushl	$40
	pushl	$30
	pushl	$20
	pushl	$10
	pushl	$0
	pushl	$_s11
	call	_printf
	leal	48(%esp),%esp
	pushl	$10
	call	_f0
	leal	4(%esp),%esp
	pushl	$10
	call	_f1
	leal	4(%esp),%esp
	pushl	$10
	call	_f2
	leal	4(%esp),%esp
	movl	$0,%eax
	leave
	ret

	.data
	.align	1
	.global	_s11
_s11:
	.byte	37
	.byte	117
	.byte	32
	.byte	37
	.byte	117
	.byte	32
	.byte	37
	.byte	117
	.byte	32
	.byte	37
	.byte	117
	.byte	32
	.byte	37
	.byte	117
	.byte	32
	.byte	37
	.byte	117
	.byte	32
	.byte	37
	.byte	117
	.byte	32
	.byte	37
	.byte	117
	.byte	32
	.byte	37
	.byte	117
	.byte	32
	.byte	37
	.byte	117
	.byte	32
	.byte	37
	.byte	117
	.byte	10
	.byte	0
	.align	4
	.global	_a0
_a0:
	.long	0
	.align	4
	.global	_a1
_a1:
	.long	10
	.align	4
	.global	_a2
_a2:
	.long	20
	.align	4
	.global	_a3
_a3:
	.long	30
	.align	4
	.global	_a4
_a4:
	.long	40
	.align	4
	.global	_a5
_a5:
	.long	50
	.align	4
	.global	_a6
_a6:
	.long	60
	.align	4
	.global	_a7
_a7:
	.long	860
	.align	4
	.global	_a8
_a8:
	.long	1710
	.align	4
	.global	_a9
_a9:
	.long	2147483640
	.align	4
	.global	_aa
_aa:
	.long	10
