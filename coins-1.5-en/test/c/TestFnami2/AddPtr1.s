 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */

	.text
	.align	4
	.global	_f0
_f0:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%ecx
	movswl	12(%ecx),%eax
	pushl	%eax
	movswl	4(%ecx),%eax
	pushl	%eax
	movswl	12(%ecx),%eax
	pushl	%eax
	movswl	4(%ecx),%eax
	pushl	%eax
	pushl	$_s4
	call	_printf
	leal	20(%esp),%esp
.L3:
	leave
	ret


	.align	4
	.global	_g0
_g0:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%ecx
	movswl	18(%ecx),%eax
	pushl	%eax
	movswl	6(%ecx),%eax
	pushl	%eax
	movswl	18(%ecx),%eax
	pushl	%eax
	movswl	6(%ecx),%eax
	pushl	%eax
	pushl	$_s4
	call	_printf
	leal	20(%esp),%esp
.L6:
	leave
	ret


	.align	4
	.global	_op0
_op0:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%ecx
	movl	12(%ebp),%eax
	leal	(%ecx,%eax,4),%eax
	leave
	ret


	.align	4
	.global	_op1
_op1:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%ecx
	movl	12(%ebp),%eax
	imull	$6,%eax
	leal	(%ecx,%eax),%eax
	leave
	ret


	.align	4
	.global	_op2
_op2:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%ecx
	movl	12(%ebp),%eax
	leal	(%ecx,%eax,4),%eax
	leave
	ret


	.align	4
	.global	_op3
_op3:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%ecx
	movl	12(%ebp),%eax
	imull	$6,%eax
	leal	(%ecx,%eax),%eax
	leave
	ret


	.align	4
	.global	_f2
_f2:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$4,%esp
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	movl	8(%ebp),%edi
	pushl	$2
	pushl	%edi
	call	_op0
	movl	%eax,-4(%ebp)
	leal	8(%esp),%esp
	pushl	$4
	pushl	%edi
	call	_op0
	movl	%eax,%esi
	leal	8(%esp),%esp
	pushl	$2
	pushl	%edi
	call	_op2
	movl	%eax,%ebx
	leal	8(%esp),%esp
	pushl	$4
	pushl	%edi
	call	_op2
	leal	8(%esp),%esp
	movswl	-4(%eax),%eax
	pushl	%eax
	movswl	-4(%ebx),%eax
	pushl	%eax
	movswl	-4(%esi),%eax
	pushl	%eax
	movl	-4(%ebp),%eax
	movswl	-4(%eax),%eax
	pushl	%eax
	pushl	$_s4
	call	_printf
	leal	20(%esp),%esp
.L21:
	popl	%edi
	popl	%esi
	popl	%ebx
	leave
	ret


	.align	4
	.global	_g2
_g2:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$4,%esp
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	movl	8(%ebp),%edi
	pushl	$2
	pushl	%edi
	call	_op1
	movl	%eax,-4(%ebp)
	leal	8(%esp),%esp
	pushl	$4
	pushl	%edi
	call	_op1
	movl	%eax,%esi
	leal	8(%esp),%esp
	pushl	$2
	pushl	%edi
	call	_op3
	movl	%eax,%ebx
	leal	8(%esp),%esp
	pushl	$4
	pushl	%edi
	call	_op3
	leal	8(%esp),%esp
	movswl	-6(%eax),%eax
	pushl	%eax
	movswl	-6(%ebx),%eax
	pushl	%eax
	movswl	-6(%esi),%eax
	pushl	%eax
	movl	-4(%ebp),%eax
	movswl	-6(%eax),%eax
	pushl	%eax
	pushl	$_s4
	call	_printf
	leal	20(%esp),%esp
.L24:
	popl	%edi
	popl	%esi
	popl	%ebx
	leave
	ret


	.align	4
	.global	_main0g
_main0g:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$8,%esp
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	movl	$_sa+8,%ecx
	movl	$_sa+16,%edx
	movl	$_sa+8,%edi
	movl	$_sa+16,%eax
	movl	$_aa+12,-8(%ebp)
	movl	$_aa+24,-4(%ebp)
	movl	$_aa+12,%ebx
	movl	$_aa+24,%esi
	movswl	-4(%eax),%eax
	pushl	%eax
	movswl	-4(%edi),%eax
	pushl	%eax
	movswl	-4(%edx),%eax
	pushl	%eax
	movswl	-4(%ecx),%eax
	pushl	%eax
	pushl	$_s4
	call	_printf
	leal	20(%esp),%esp
	movswl	-6(%esi),%eax
	pushl	%eax
	movswl	-6(%ebx),%eax
	pushl	%eax
	movl	-4(%ebp),%eax
	movswl	-6(%eax),%eax
	pushl	%eax
	movl	-8(%ebp),%eax
	movswl	-6(%eax),%eax
	pushl	%eax
	pushl	$_s4
	call	_printf
	leal	20(%esp),%esp
.L27:
	popl	%edi
	popl	%esi
	popl	%ebx
	leave
	ret


	.align	4
	.global	_main0l
_main0l:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$48,%esp
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	movw	$0,-16(%ebp)
	movw	$1,-14(%ebp)
	movw	$2,-12(%ebp)
	movw	$3,-10(%ebp)
	movw	$4,-8(%ebp)
	movw	$5,-6(%ebp)
	movw	$6,-4(%ebp)
	movw	$7,-2(%ebp)
	movw	$0,-40(%ebp)
	movw	$1,-38(%ebp)
	movw	$2,-36(%ebp)
	movw	$3,-34(%ebp)
	movw	$4,-32(%ebp)
	movw	$5,-30(%ebp)
	movw	$6,-28(%ebp)
	movw	$7,-26(%ebp)
	movw	$8,-24(%ebp)
	movw	$9,-22(%ebp)
	movw	$10,-20(%ebp)
	movw	$11,-18(%ebp)
	leal	-8(%ebp),%edi
	movl	%ebp,%edx
	leal	-8(%ebp),%ecx
	movl	%ebp,%eax
	leal	-28(%ebp),%ebx
	movl	%ebx,-48(%ebp)
	leal	-16(%ebp),%ebx
	movl	%ebx,-44(%ebp)
	leal	-28(%ebp),%esi
	leal	-16(%ebp),%ebx
	movswl	-4(%eax),%eax
	pushl	%eax
	movswl	-4(%ecx),%eax
	pushl	%eax
	movswl	-4(%edx),%eax
	pushl	%eax
	movswl	-4(%edi),%eax
	pushl	%eax
	pushl	$_s4
	call	_printf
	leal	20(%esp),%esp
	movswl	-6(%ebx),%eax
	pushl	%eax
	movswl	-6(%esi),%eax
	pushl	%eax
	movl	-44(%ebp),%eax
	movswl	-6(%eax),%eax
	pushl	%eax
	movl	-48(%ebp),%eax
	movswl	-6(%eax),%eax
	pushl	%eax
	pushl	$_s4
	call	_printf
	leal	20(%esp),%esp
.L30:
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
	pushl	%ebx
	movl	_p3,%eax
	movswl	-4(%eax),%eax
	pushl	%eax
	movl	_p2,%eax
	movswl	-4(%eax),%eax
	pushl	%eax
	movl	_p1,%eax
	movswl	-4(%eax),%eax
	pushl	%eax
	movl	_p0,%eax
	movswl	-4(%eax),%eax
	pushl	%eax
	pushl	$_s4
	call	_printf
	leal	20(%esp),%esp
	movl	_q3,%eax
	movswl	-6(%eax),%eax
	pushl	%eax
	movl	_q2,%eax
	movswl	-6(%eax),%eax
	pushl	%eax
	movl	_q1,%eax
	movswl	-6(%eax),%eax
	pushl	%eax
	movl	_q0,%eax
	movswl	-6(%eax),%eax
	pushl	%eax
	pushl	$_s4
	call	_printf
	leal	20(%esp),%esp
	call	_main0g
	call	_main0l
	movl	$_sa,%ebx
	movswl	12(%ebx),%eax
	pushl	%eax
	movswl	4(%ebx),%eax
	pushl	%eax
	movswl	12(%ebx),%eax
	pushl	%eax
	movswl	4(%ebx),%eax
	pushl	%eax
	pushl	$_s4
	call	_printf
	leal	20(%esp),%esp
	movswl	12(%ebx),%eax
	pushl	%eax
	movswl	4(%ebx),%eax
	pushl	%eax
	movswl	12(%ebx),%eax
	pushl	%eax
	movswl	4(%ebx),%eax
	pushl	%eax
	pushl	$_s4
	call	_printf
	leal	20(%esp),%esp
	movl	$_aa,%ebx
	movswl	18(%ebx),%eax
	pushl	%eax
	movswl	6(%ebx),%eax
	pushl	%eax
	movswl	18(%ebx),%eax
	pushl	%eax
	movswl	6(%ebx),%eax
	pushl	%eax
	pushl	$_s4
	call	_printf
	leal	20(%esp),%esp
	movswl	18(%ebx),%eax
	pushl	%eax
	movswl	6(%ebx),%eax
	pushl	%eax
	movswl	18(%ebx),%eax
	pushl	%eax
	movswl	6(%ebx),%eax
	pushl	%eax
	pushl	$_s4
	call	_printf
	leal	20(%esp),%esp
	pushl	$_sa
	call	_f0
	leal	4(%esp),%esp
	pushl	$_aa
	call	_g0
	leal	4(%esp),%esp
	pushl	$_sa
	call	_f2
	leal	4(%esp),%esp
	pushl	$_aa
	call	_g2
	leal	4(%esp),%esp
	movl	$0,%eax
	popl	%ebx
	leave
	ret

	.data
	.align	1
	.global	_s4
_s4:
	.byte	37
	.byte	100
	.byte	32
	.byte	37
	.byte	100
	.byte	32
	.byte	37
	.byte	100
	.byte	32
	.byte	37
	.byte	100
	.byte	10
	.byte	0
	.align	2
	.global	_sa
_sa:
	.short	0
	.short	1
	.short	2
	.short	3
	.short	4
	.short	5
	.short	6
	.short	7
	.align	2
	.global	_aa
_aa:
	.short	0
	.short	1
	.short	2
	.short	3
	.short	4
	.short	5
	.short	6
	.short	7
	.short	8
	.short	9
	.short	10
	.short	11
	.align	4
	.global	_p0
_p0:
	.long	_sa+8
	.align	4
	.global	_p1
_p1:
	.long	_sa+16
	.align	4
	.global	_p2
_p2:
	.long	_sa+8
	.align	4
	.global	_p3
_p3:
	.long	_sa+16
	.align	4
	.global	_q0
_q0:
	.long	_aa+12
	.align	4
	.global	_q1
_q1:
	.long	_aa+24
	.align	4
	.global	_q2
_q2:
	.long	_aa+12
	.align	4
	.global	_q3
_q3:
	.long	_aa+24
