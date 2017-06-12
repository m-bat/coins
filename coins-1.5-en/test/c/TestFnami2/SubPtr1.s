 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */

	.text
	.align	4
	.global	_f0
_f0:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%ecx
	movswl	-16(%ecx),%eax
	pushl	%eax
	movswl	-8(%ecx),%eax
	pushl	%eax
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
.L3:
	leave
	ret


	.align	4
	.global	_g0
_g0:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%ecx
	movswl	-24(%ecx),%eax
	pushl	%eax
	movswl	-12(%ecx),%eax
	pushl	%eax
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
.L6:
	leave
	ret


	.align	4
	.global	_op0
_op0:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%eax
	movl	12(%ebp),%ecx
	leal	(,%ecx,4),%ecx
	subl	%ecx,%eax
	leave
	ret


	.align	4
	.global	_op1
_op1:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%eax
	movl	12(%ebp),%ecx
	imull	$6,%ecx
	subl	%ecx,%eax
	leave
	ret


	.align	4
	.global	_f2
_f2:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	pushl	%esi
	movl	8(%ebp),%esi
	pushl	$2
	pushl	%esi
	call	_op0
	movl	%eax,%ebx
	leal	8(%esp),%esp
	pushl	$4
	pushl	%esi
	call	_op0
	leal	8(%esp),%esp
	movswl	(%eax),%eax
	pushl	%eax
	movswl	(%ebx),%eax
	pushl	%eax
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
.L15:
	popl	%esi
	popl	%ebx
	leave
	ret


	.align	4
	.global	_g2
_g2:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	pushl	%esi
	movl	8(%ebp),%esi
	pushl	$2
	pushl	%esi
	call	_op1
	movl	%eax,%ebx
	leal	8(%esp),%esp
	pushl	$4
	pushl	%esi
	call	_op1
	leal	8(%esp),%esp
	movswl	(%eax),%eax
	pushl	%eax
	movswl	(%ebx),%eax
	pushl	%eax
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
.L18:
	popl	%esi
	popl	%ebx
	leave
	ret


	.align	4
	.global	_main0g
_main0g:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	pushl	%esi
	movl	$_sa+16-8,%ecx
	movl	$_sa+16-16,%eax
	movl	$_aa+24-12,%ebx
	movl	$_aa+24-24,%esi
	movswl	(%eax),%eax
	pushl	%eax
	movswl	(%ecx),%eax
	pushl	%eax
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
	movswl	(%esi),%eax
	pushl	%eax
	movswl	(%ebx),%eax
	pushl	%eax
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
.L21:
	popl	%esi
	popl	%ebx
	leave
	ret


	.align	4
	.global	_main0l
_main0l:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$40,%esp
	pushl	%ebx
	pushl	%esi
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
	leal	-8(%ebp),%ecx
	leal	-16(%ebp),%edx
	leal	-16(%ebp),%eax
	leal	-12(%eax),%ebx
	leal	-16(%ebp),%eax
	leal	-24(%eax),%esi
	movswl	(%edx),%eax
	pushl	%eax
	movswl	(%ecx),%eax
	pushl	%eax
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
	movswl	(%esi),%eax
	pushl	%eax
	movswl	(%ebx),%eax
	pushl	%eax
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
.L24:
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
	movl	_p1,%eax
	movswl	(%eax),%eax
	pushl	%eax
	movl	_p0,%eax
	movswl	(%eax),%eax
	pushl	%eax
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
	movl	_q1,%eax
	movswl	(%eax),%eax
	pushl	%eax
	movl	_q0,%eax
	movswl	(%eax),%eax
	pushl	%eax
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
	call	_main0g
	call	_main0l
	movl	$_sa+16,%ebx
	movswl	-16(%ebx),%eax
	pushl	%eax
	movswl	-8(%ebx),%eax
	pushl	%eax
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
	movswl	-16(%ebx),%eax
	pushl	%eax
	movswl	-8(%ebx),%eax
	pushl	%eax
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
	movl	$_aa+24,%ebx
	movswl	-24(%ebx),%eax
	pushl	%eax
	movswl	-12(%ebx),%eax
	pushl	%eax
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
	movswl	-24(%ebx),%eax
	pushl	%eax
	movswl	-12(%ebx),%eax
	pushl	%eax
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
	pushl	$_sa+16
	call	_f0
	leal	4(%esp),%esp
	pushl	$_aa+24
	call	_g0
	leal	4(%esp),%esp
	pushl	$_sa+16
	call	_f2
	leal	4(%esp),%esp
	pushl	$_aa+24
	call	_g2
	leal	4(%esp),%esp
	movl	$0,%eax
	popl	%ebx
	leave
	ret

	.data
	.align	1
	.global	_s2
_s2:
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
	.long	_sa+16-8
	.align	4
	.global	_p1
_p1:
	.long	_sa+16-16
	.align	4
	.global	_q0
_q0:
	.long	_aa+24-12
	.align	4
	.global	_q1
_q1:
	.long	_aa+24-24
