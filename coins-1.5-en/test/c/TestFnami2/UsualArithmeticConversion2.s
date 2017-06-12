 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */

	.text
	.align	4
	.global	_f1d
_f1d:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$24,%esp
	pushl	%ebx
	pushl	%esi
	movl	8(%ebp),%eax
	movl	12(%ebp),%ebx
	movl	16(%ebp),%edx
	movl	20(%ebp),%ecx
	flds	24(%ebp)
	fstps	-4(%ebp)
	fldl	.LC1
	movl	%eax,-24(%ebp)
	fildl	-24(%ebp)
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	jne	.L4
.L3:
	movl	$1,%esi
	jmp	.L5
.L4:
	movl	$0,%esi
.L5:
	movl	%ebx,-24(%ebp)
	fildl	-24(%ebp)
	fstpl	-12(%ebp)
	cmpl	$0,%ebx
	jge	.L304
.L305:
	fldl	-12(%ebp)
	faddl	.LC2
	fstpl	-12(%ebp)
.L304:
	fldl	.LC3
	fcompl	-12(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	jne	.L7
.L6:
	movl	$1,%ebx
	jmp	.L8
.L7:
	movl	$0,%ebx
.L8:
	fldl	.LC4
	movl	%edx,-24(%ebp)
	fildl	-24(%ebp)
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	jne	.L10
.L9:
	movl	$1,%edx
	jmp	.L11
.L10:
	movl	$0,%edx
.L11:
	movl	%ecx,-24(%ebp)
	fildl	-24(%ebp)
	fstpl	-20(%ebp)
	cmpl	$0,%ecx
	jge	.L306
.L307:
	fldl	-20(%ebp)
	faddl	.LC2
	fstpl	-20(%ebp)
.L306:
	fldl	.LC5
	fcompl	-20(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	jne	.L13
.L12:
	movl	$1,%ecx
	jmp	.L14
.L13:
	movl	$0,%ecx
.L14:
	fldl	.LC6
	flds	-4(%ebp)
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	je	.L16
.L15:
	movl	$1,%eax
	jmp	.L17
.L16:
	movl	$0,%eax
.L17:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%ebx
	pushl	%esi
	pushl	$_s5
	call	_printf
	leal	24(%esp),%esp
.L18:
	popl	%esi
	popl	%ebx
	leave
	ret


	.align	4
	.global	_f1f
_f1f:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$12,%esp
	pushl	%ebx
	pushl	%esi
	movl	8(%ebp),%eax
	movl	12(%ebp),%edx
	movl	16(%ebp),%ecx
	movl	20(%ebp),%esi
	flds	.LC7
	movl	%eax,-12(%ebp)
	fildl	-12(%ebp)
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	jne	.L22
.L21:
	movl	$1,%ebx
	jmp	.L23
.L22:
	movl	$0,%ebx
.L23:
	movl	%edx,-12(%ebp)
	fildl	-12(%ebp)
	fstps	-4(%ebp)
	cmpl	$0,%edx
	jge	.L308
.L309:
	flds	-4(%ebp)
	fadds	.LC8
	fstps	-4(%ebp)
.L308:
	flds	.LC9
	fcomps	-4(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	jne	.L25
.L24:
	movl	$1,%edx
	jmp	.L26
.L25:
	movl	$0,%edx
.L26:
	flds	.LC10
	movl	%ecx,-12(%ebp)
	fildl	-12(%ebp)
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	jne	.L28
.L27:
	movl	$1,%ecx
	jmp	.L29
.L28:
	movl	$0,%ecx
.L29:
	movl	%esi,-12(%ebp)
	fildl	-12(%ebp)
	fstps	-8(%ebp)
	cmpl	$0,%esi
	jge	.L310
.L311:
	flds	-8(%ebp)
	fadds	.LC8
	fstps	-8(%ebp)
.L310:
	flds	.LC11
	fcomps	-8(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	jne	.L31
.L30:
	movl	$1,%eax
	jmp	.L32
.L31:
	movl	$0,%eax
.L32:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%ebx
	pushl	$_s4
	call	_printf
	leal	20(%esp),%esp
.L33:
	popl	%esi
	popl	%ebx
	leave
	ret


	.align	4
	.global	_f1ul
_f1ul:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%edx
	movl	12(%ebp),%ecx
	movl	16(%ebp),%eax
	cmpl	$19,%edx
	jbe	.L37
.L36:
	movl	$1,%edx
	jmp	.L38
.L37:
	movl	$0,%edx
.L38:
	cmpl	$20,%ecx
	jbe	.L40
.L39:
	movl	$1,%ecx
	jmp	.L41
.L40:
	movl	$0,%ecx
.L41:
	cmpl	$21,%eax
	jbe	.L43
.L42:
	movl	$1,%eax
	jmp	.L44
.L43:
	movl	$0,%eax
.L44:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
.L45:
	leave
	ret


	.align	4
	.global	_f1l
_f1l:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%ecx
	movl	12(%ebp),%eax
	cmpl	$22,%ecx
	jle	.L49
.L48:
	movl	$1,%ecx
	jmp	.L50
.L49:
	movl	$0,%ecx
.L50:
	cmpl	$23,%eax
	jbe	.L52
.L51:
	movl	$1,%eax
	jmp	.L53
.L52:
	movl	$0,%eax
.L53:
	pushl	%eax
	pushl	%ecx
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
.L54:
	leave
	ret


	.align	4
	.global	_f1ui
_f1ui:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%eax
	cmpl	$24,%eax
	jbe	.L58
.L57:
	movl	$1,%eax
	jmp	.L59
.L58:
	movl	$0,%eax
.L59:
	pushl	%eax
	pushl	$_s1
	call	_printf
	leal	8(%esp),%esp
.L60:
	leave
	ret


	.align	4
	.global	_f2d
_f2d:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$24,%esp
	pushl	%ebx
	pushl	%esi
	movl	8(%ebp),%eax
	movl	12(%ebp),%ebx
	movl	16(%ebp),%edx
	movl	20(%ebp),%ecx
	flds	24(%ebp)
	fstps	-4(%ebp)
	movl	%eax,-24(%ebp)
	fildl	-24(%ebp)
	fcompl	.LC12
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	jne	.L64
.L63:
	movl	$1,%esi
	jmp	.L65
.L64:
	movl	$0,%esi
.L65:
	movl	%ebx,-24(%ebp)
	fildl	-24(%ebp)
	fstpl	-12(%ebp)
	cmpl	$0,%ebx
	jge	.L312
.L313:
	fldl	-12(%ebp)
	faddl	.LC2
	fstpl	-12(%ebp)
.L312:
	fldl	-12(%ebp)
	fcompl	.LC13
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	jne	.L67
.L66:
	movl	$1,%ebx
	jmp	.L68
.L67:
	movl	$0,%ebx
.L68:
	movl	%edx,-24(%ebp)
	fildl	-24(%ebp)
	fcompl	.LC14
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	jne	.L70
.L69:
	movl	$1,%edx
	jmp	.L71
.L70:
	movl	$0,%edx
.L71:
	movl	%ecx,-24(%ebp)
	fildl	-24(%ebp)
	fstpl	-20(%ebp)
	cmpl	$0,%ecx
	jge	.L314
.L315:
	fldl	-20(%ebp)
	faddl	.LC2
	fstpl	-20(%ebp)
.L314:
	fldl	-20(%ebp)
	fcompl	.LC15
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	jne	.L73
.L72:
	movl	$1,%ecx
	jmp	.L74
.L73:
	movl	$0,%ecx
.L74:
	flds	-4(%ebp)
	fcompl	.LC16
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	jne	.L76
.L75:
	movl	$1,%eax
	jmp	.L77
.L76:
	movl	$0,%eax
.L77:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%ebx
	pushl	%esi
	pushl	$_s5
	call	_printf
	leal	24(%esp),%esp
.L78:
	popl	%esi
	popl	%ebx
	leave
	ret


	.align	4
	.global	_f2f
_f2f:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$12,%esp
	pushl	%ebx
	pushl	%esi
	movl	8(%ebp),%eax
	movl	12(%ebp),%edx
	movl	16(%ebp),%ecx
	movl	20(%ebp),%esi
	movl	%eax,-12(%ebp)
	fildl	-12(%ebp)
	fcomps	.LC17
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	jne	.L82
.L81:
	movl	$1,%ebx
	jmp	.L83
.L82:
	movl	$0,%ebx
.L83:
	movl	%edx,-12(%ebp)
	fildl	-12(%ebp)
	fstps	-4(%ebp)
	cmpl	$0,%edx
	jge	.L316
.L317:
	flds	-4(%ebp)
	fadds	.LC8
	fstps	-4(%ebp)
.L316:
	flds	-4(%ebp)
	fcomps	.LC18
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	jne	.L85
.L84:
	movl	$1,%edx
	jmp	.L86
.L85:
	movl	$0,%edx
.L86:
	movl	%ecx,-12(%ebp)
	fildl	-12(%ebp)
	fcomps	.LC19
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	jne	.L88
.L87:
	movl	$1,%ecx
	jmp	.L89
.L88:
	movl	$0,%ecx
.L89:
	movl	%esi,-12(%ebp)
	fildl	-12(%ebp)
	fstps	-8(%ebp)
	cmpl	$0,%esi
	jge	.L318
.L319:
	flds	-8(%ebp)
	fadds	.LC8
	fstps	-8(%ebp)
.L318:
	flds	-8(%ebp)
	fcomps	.LC20
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	jne	.L91
.L90:
	movl	$1,%eax
	jmp	.L92
.L91:
	movl	$0,%eax
.L92:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%ebx
	pushl	$_s4
	call	_printf
	leal	20(%esp),%esp
.L93:
	popl	%esi
	popl	%ebx
	leave
	ret


	.align	4
	.global	_f2ul
_f2ul:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%edx
	movl	12(%ebp),%ecx
	movl	16(%ebp),%eax
	cmpl	$39,%edx
	jae	.L97
.L96:
	movl	$1,%edx
	jmp	.L98
.L97:
	movl	$0,%edx
.L98:
	cmpl	$40,%ecx
	jae	.L100
.L99:
	movl	$1,%ecx
	jmp	.L101
.L100:
	movl	$0,%ecx
.L101:
	cmpl	$41,%eax
	jae	.L103
.L102:
	movl	$1,%eax
	jmp	.L104
.L103:
	movl	$0,%eax
.L104:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
.L105:
	leave
	ret


	.align	4
	.global	_f2l
_f2l:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%ecx
	movl	12(%ebp),%eax
	cmpl	$42,%ecx
	jge	.L109
.L108:
	movl	$1,%ecx
	jmp	.L110
.L109:
	movl	$0,%ecx
.L110:
	cmpl	$43,%eax
	jae	.L112
.L111:
	movl	$1,%eax
	jmp	.L113
.L112:
	movl	$0,%eax
.L113:
	pushl	%eax
	pushl	%ecx
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
.L114:
	leave
	ret


	.align	4
	.global	_f2ui
_f2ui:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%eax
	cmpl	$44,%eax
	jae	.L118
.L117:
	movl	$1,%eax
	jmp	.L119
.L118:
	movl	$0,%eax
.L119:
	pushl	%eax
	pushl	$_s1
	call	_printf
	leal	8(%esp),%esp
.L120:
	leave
	ret


	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$144,%esp
	pushl	%ebx
	pushl	%esi
	pushl	%edi
	pushl	_d_f
	pushl	_d_ul
	pushl	_d_l
	pushl	_d_ui
	pushl	_d_i
	pushl	$_s5
	call	_printf
	leal	24(%esp),%esp
	movl	$-50,%ebx
	movl	$-1294967295,-132(%ebp)
	movl	$-52,-136(%ebp)
	movl	$-1294967293,-140(%ebp)
	flds	.LC21
	fstps	-4(%ebp)
	movl	$0,%edx
	fildl	-132(%ebp)
	fstpl	-12(%ebp)
	movl	-132(%ebp),%eax
	cmpl	$0,%eax
	jge	.L320
.L321:
	fldl	-12(%ebp)
	faddl	.LC2
	fstpl	-12(%ebp)
.L320:
	fldl	.LC3
	fcompl	-12(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	jne	.L127
.L126:
	movl	$1,%esi
	jmp	.L128
.L127:
	movl	$0,%esi
.L128:
	fldl	.LC4
	fildl	-136(%ebp)
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	jne	.L130
.L129:
	movl	$1,%ecx
	jmp	.L131
.L130:
	movl	$0,%ecx
.L131:
	fildl	-140(%ebp)
	fstpl	-20(%ebp)
	movl	-140(%ebp),%eax
	cmpl	$0,%eax
	jge	.L322
.L323:
	fldl	-20(%ebp)
	faddl	.LC2
	fstpl	-20(%ebp)
.L322:
	fldl	.LC5
	fcompl	-20(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	jne	.L133
.L132:
	movl	$1,%edi
	jmp	.L134
.L133:
	movl	$0,%edi
.L134:
	fldl	.LC6
	flds	-4(%ebp)
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	je	.L136
.L135:
	movl	$1,%eax
	jmp	.L137
.L136:
	movl	$0,%eax
.L137:
	pushl	%eax
	pushl	%edi
	pushl	%ecx
	pushl	%esi
	pushl	%edx
	pushl	$_s5
	call	_printf
	leal	24(%esp),%esp
	fldl	.LC1
	movl	%ebx,-144(%ebp)
	fildl	-144(%ebp)
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	jne	.L139
.L138:
	movl	$1,%edx
	jmp	.L140
.L139:
	movl	$0,%edx
.L140:
	fildl	-132(%ebp)
	fstpl	-28(%ebp)
	movl	-132(%ebp),%eax
	cmpl	$0,%eax
	jge	.L324
.L325:
	fldl	-28(%ebp)
	faddl	.LC2
	fstpl	-28(%ebp)
.L324:
	fldl	.LC3
	fcompl	-28(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	jne	.L142
.L141:
	movl	$1,%ebx
	jmp	.L143
.L142:
	movl	$0,%ebx
.L143:
	fldl	.LC4
	fildl	-136(%ebp)
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	jne	.L145
.L144:
	movl	$1,%ecx
	jmp	.L146
.L145:
	movl	$0,%ecx
.L146:
	fildl	-140(%ebp)
	fstpl	-36(%ebp)
	movl	-140(%ebp),%eax
	cmpl	$0,%eax
	jge	.L326
.L327:
	fldl	-36(%ebp)
	faddl	.LC2
	fstpl	-36(%ebp)
.L326:
	fldl	.LC5
	fcompl	-36(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	jne	.L148
.L147:
	movl	$1,%esi
	jmp	.L149
.L148:
	movl	$0,%esi
.L149:
	fldl	.LC6
	flds	-4(%ebp)
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	je	.L151
.L150:
	movl	$1,%eax
	jmp	.L152
.L151:
	movl	$0,%eax
.L152:
	pushl	%eax
	pushl	%esi
	pushl	%ecx
	pushl	%ebx
	pushl	%edx
	pushl	$_s5
	call	_printf
	leal	24(%esp),%esp
	pushl	.LC21
	pushl	$-1294967293
	pushl	$-52
	pushl	$-1294967295
	pushl	$-50
	call	_f1d
	leal	20(%esp),%esp
	pushl	_f_ul
	pushl	_f_l
	pushl	_f_ui
	pushl	_f_i
	pushl	$_s4
	call	_printf
	leal	20(%esp),%esp
	movl	$-55,%esi
	movl	$-1294967290,%ebx
	movl	$-57,-112(%ebp)
	movl	$-1294967288,-120(%ebp)
	movl	$0,%edi
	movl	%ebx,-144(%ebp)
	fildl	-144(%ebp)
	fstps	-40(%ebp)
	cmpl	$0,%ebx
	jge	.L328
.L329:
	flds	-40(%ebp)
	fadds	.LC8
	fstps	-40(%ebp)
.L328:
	flds	.LC9
	fcomps	-40(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	jne	.L157
.L156:
	movl	$1,%edx
	jmp	.L158
.L157:
	movl	$0,%edx
.L158:
	flds	.LC10
	fildl	-112(%ebp)
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	jne	.L160
.L159:
	movl	$1,%ecx
	jmp	.L161
.L160:
	movl	$0,%ecx
.L161:
	fildl	-120(%ebp)
	fstps	-44(%ebp)
	movl	-120(%ebp),%eax
	cmpl	$0,%eax
	jge	.L330
.L331:
	flds	-44(%ebp)
	fadds	.LC8
	fstps	-44(%ebp)
.L330:
	flds	.LC11
	fcomps	-44(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	jne	.L163
.L162:
	movl	$1,%eax
	jmp	.L164
.L163:
	movl	$0,%eax
.L164:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%edi
	pushl	$_s4
	call	_printf
	leal	20(%esp),%esp
	flds	.LC7
	movl	%esi,-144(%ebp)
	fildl	-144(%ebp)
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	jne	.L166
.L165:
	movl	$1,%edx
	jmp	.L167
.L166:
	movl	$0,%edx
.L167:
	movl	%ebx,-144(%ebp)
	fildl	-144(%ebp)
	fstps	-48(%ebp)
	cmpl	$0,%ebx
	jge	.L332
.L333:
	flds	-48(%ebp)
	fadds	.LC8
	fstps	-48(%ebp)
.L332:
	flds	.LC9
	fcomps	-48(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	jne	.L169
.L168:
	movl	$1,%ebx
	jmp	.L170
.L169:
	movl	$0,%ebx
.L170:
	flds	.LC10
	fildl	-112(%ebp)
	fxch
	fcompp
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	jne	.L172
.L171:
	movl	$1,%ecx
	jmp	.L173
.L172:
	movl	$0,%ecx
.L173:
	fildl	-120(%ebp)
	fstps	-52(%ebp)
	movl	-120(%ebp),%eax
	cmpl	$0,%eax
	jge	.L334
.L335:
	flds	-52(%ebp)
	fadds	.LC8
	fstps	-52(%ebp)
.L334:
	flds	.LC11
	fcomps	-52(%ebp)
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	jne	.L175
.L174:
	movl	$1,%eax
	jmp	.L176
.L175:
	movl	$0,%eax
.L176:
	pushl	%eax
	pushl	%ecx
	pushl	%ebx
	pushl	%edx
	pushl	$_s4
	call	_printf
	leal	20(%esp),%esp
	pushl	$-1294967288
	pushl	$-57
	pushl	$-1294967290
	pushl	$-55
	call	_f1f
	leal	16(%esp),%esp
	pushl	_ul_l
	pushl	_ul_ui
	pushl	_ul_i
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	movl	$-59,%edi
	movl	$-1294967286,%esi
	movl	$-61,%ebx
	movl	$1,%edx
	cmpl	$20,%esi
	jbe	.L181
.L180:
	movl	$1,%ecx
	jmp	.L182
.L181:
	movl	$0,%ecx
.L182:
	cmpl	$21,%ebx
	jbe	.L184
.L183:
	movl	$1,%eax
	jmp	.L185
.L184:
	movl	$0,%eax
.L185:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	cmpl	$19,%edi
	jbe	.L187
.L186:
	movl	$1,%edx
	jmp	.L188
.L187:
	movl	$0,%edx
.L188:
	cmpl	$20,%esi
	jbe	.L190
.L189:
	movl	$1,%ecx
	jmp	.L191
.L190:
	movl	$0,%ecx
.L191:
	cmpl	$21,%ebx
	jbe	.L193
.L192:
	movl	$1,%eax
	jmp	.L194
.L193:
	movl	$0,%eax
.L194:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	pushl	$-61
	pushl	$-1294967286
	pushl	$-59
	call	_f1ul
	leal	12(%esp),%esp
	pushl	_l_ui
	pushl	_l_i
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
	movl	$-62,%esi
	movl	$-1294967283,%ebx
	movl	$0,%ecx
	cmpl	$23,%ebx
	jbe	.L199
.L198:
	movl	$1,%eax
	jmp	.L200
.L199:
	movl	$0,%eax
.L200:
	pushl	%eax
	pushl	%ecx
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
	cmpl	$22,%esi
	jle	.L202
.L201:
	movl	$1,%ecx
	jmp	.L203
.L202:
	movl	$0,%ecx
.L203:
	cmpl	$23,%ebx
	jbe	.L205
.L204:
	movl	$1,%eax
	jmp	.L206
.L205:
	movl	$0,%eax
.L206:
	pushl	%eax
	pushl	%ecx
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
	pushl	$-1294967283
	pushl	$-62
	call	_f1l
	leal	8(%esp),%esp
	pushl	_ui_i
	pushl	$_s1
	call	_printf
	leal	8(%esp),%esp
	movl	$-64,%ebx
	movl	$1,%eax
	pushl	%eax
	pushl	$_s1
	call	_printf
	leal	8(%esp),%esp
	cmpl	$24,%ebx
	jbe	.L211
.L210:
	movl	$1,%eax
	jmp	.L212
.L211:
	movl	$0,%eax
.L212:
	pushl	%eax
	pushl	$_s1
	call	_printf
	leal	8(%esp),%esp
	pushl	$-64
	call	_f1ui
	leal	4(%esp),%esp
	pushl	_f_d
	pushl	_ul_d
	pushl	_l_d
	pushl	_ui_d
	pushl	_i_d
	pushl	$_s5
	call	_printf
	leal	24(%esp),%esp
	movl	$-70,%ebx
	movl	$-1294967275,-116(%ebp)
	movl	$-72,-124(%ebp)
	movl	$-1294967273,-128(%ebp)
	flds	.LC22
	fstps	-4(%ebp)
	movl	$1,%edx
	fildl	-116(%ebp)
	fstpl	-60(%ebp)
	movl	-116(%ebp),%eax
	cmpl	$0,%eax
	jge	.L336
.L337:
	fldl	-60(%ebp)
	faddl	.LC2
	fstpl	-60(%ebp)
.L336:
	fldl	-60(%ebp)
	fcompl	.LC13
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	jne	.L217
.L216:
	movl	$1,%esi
	jmp	.L218
.L217:
	movl	$0,%esi
.L218:
	fildl	-124(%ebp)
	fcompl	.LC14
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	jne	.L220
.L219:
	movl	$1,%ecx
	jmp	.L221
.L220:
	movl	$0,%ecx
.L221:
	fildl	-128(%ebp)
	fstpl	-68(%ebp)
	movl	-128(%ebp),%eax
	cmpl	$0,%eax
	jge	.L338
.L339:
	fldl	-68(%ebp)
	faddl	.LC2
	fstpl	-68(%ebp)
.L338:
	fldl	-68(%ebp)
	fcompl	.LC15
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	jne	.L223
.L222:
	movl	$1,%edi
	jmp	.L224
.L223:
	movl	$0,%edi
.L224:
	flds	-4(%ebp)
	fcompl	.LC16
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	jne	.L226
.L225:
	movl	$1,%eax
	jmp	.L227
.L226:
	movl	$0,%eax
.L227:
	pushl	%eax
	pushl	%edi
	pushl	%ecx
	pushl	%esi
	pushl	%edx
	pushl	$_s5
	call	_printf
	leal	24(%esp),%esp
	movl	%ebx,-144(%ebp)
	fildl	-144(%ebp)
	fcompl	.LC12
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	jne	.L229
.L228:
	movl	$1,%edx
	jmp	.L230
.L229:
	movl	$0,%edx
.L230:
	fildl	-116(%ebp)
	fstpl	-76(%ebp)
	movl	-116(%ebp),%eax
	cmpl	$0,%eax
	jge	.L340
.L341:
	fldl	-76(%ebp)
	faddl	.LC2
	fstpl	-76(%ebp)
.L340:
	fldl	-76(%ebp)
	fcompl	.LC13
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	jne	.L232
.L231:
	movl	$1,%ebx
	jmp	.L233
.L232:
	movl	$0,%ebx
.L233:
	fildl	-124(%ebp)
	fcompl	.LC14
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	jne	.L235
.L234:
	movl	$1,%ecx
	jmp	.L236
.L235:
	movl	$0,%ecx
.L236:
	fildl	-128(%ebp)
	fstpl	-84(%ebp)
	movl	-128(%ebp),%eax
	cmpl	$0,%eax
	jge	.L342
.L343:
	fldl	-84(%ebp)
	faddl	.LC2
	fstpl	-84(%ebp)
.L342:
	fldl	-84(%ebp)
	fcompl	.LC15
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	jne	.L238
.L237:
	movl	$1,%esi
	jmp	.L239
.L238:
	movl	$0,%esi
.L239:
	flds	-4(%ebp)
	fcompl	.LC16
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	jne	.L241
.L240:
	movl	$1,%eax
	jmp	.L242
.L241:
	movl	$0,%eax
.L242:
	pushl	%eax
	pushl	%esi
	pushl	%ecx
	pushl	%ebx
	pushl	%edx
	pushl	$_s5
	call	_printf
	leal	24(%esp),%esp
	pushl	.LC22
	pushl	$-1294967273
	pushl	$-72
	pushl	$-1294967275
	pushl	$-70
	call	_f2d
	leal	20(%esp),%esp
	pushl	_ul_f
	pushl	_l_f
	pushl	_ui_f
	pushl	_i_f
	pushl	$_s4
	call	_printf
	leal	20(%esp),%esp
	movl	$-75,%esi
	movl	$-1294967270,%ebx
	movl	$-77,-104(%ebp)
	movl	$-1294967268,-108(%ebp)
	movl	$1,%edi
	movl	%ebx,-144(%ebp)
	fildl	-144(%ebp)
	fstps	-88(%ebp)
	cmpl	$0,%ebx
	jge	.L344
.L345:
	flds	-88(%ebp)
	fadds	.LC8
	fstps	-88(%ebp)
.L344:
	flds	-88(%ebp)
	fcomps	.LC18
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	jne	.L247
.L246:
	movl	$1,%edx
	jmp	.L248
.L247:
	movl	$0,%edx
.L248:
	fildl	-104(%ebp)
	fcomps	.LC19
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	jne	.L250
.L249:
	movl	$1,%ecx
	jmp	.L251
.L250:
	movl	$0,%ecx
.L251:
	fildl	-108(%ebp)
	fstps	-92(%ebp)
	movl	-108(%ebp),%eax
	cmpl	$0,%eax
	jge	.L346
.L347:
	flds	-92(%ebp)
	fadds	.LC8
	fstps	-92(%ebp)
.L346:
	flds	-92(%ebp)
	fcomps	.LC20
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	jne	.L253
.L252:
	movl	$1,%eax
	jmp	.L254
.L253:
	movl	$0,%eax
.L254:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	%edi
	pushl	$_s4
	call	_printf
	leal	20(%esp),%esp
	movl	%esi,-144(%ebp)
	fildl	-144(%ebp)
	fcomps	.LC17
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	jne	.L256
.L255:
	movl	$1,%edx
	jmp	.L257
.L256:
	movl	$0,%edx
.L257:
	movl	%ebx,-144(%ebp)
	fildl	-144(%ebp)
	fstps	-96(%ebp)
	cmpl	$0,%ebx
	jge	.L348
.L349:
	flds	-96(%ebp)
	fadds	.LC8
	fstps	-96(%ebp)
.L348:
	flds	-96(%ebp)
	fcomps	.LC18
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	jne	.L259
.L258:
	movl	$1,%ebx
	jmp	.L260
.L259:
	movl	$0,%ebx
.L260:
	fildl	-104(%ebp)
	fcomps	.LC19
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	jne	.L262
.L261:
	movl	$1,%ecx
	jmp	.L263
.L262:
	movl	$0,%ecx
.L263:
	fildl	-108(%ebp)
	fstps	-100(%ebp)
	movl	-108(%ebp),%eax
	cmpl	$0,%eax
	jge	.L350
.L351:
	flds	-100(%ebp)
	fadds	.LC8
	fstps	-100(%ebp)
.L350:
	flds	-100(%ebp)
	fcomps	.LC20
	fnstsw	%ax
	andb	$69,%ah
	cmpb	$1,%ah
	jne	.L265
.L264:
	movl	$1,%eax
	jmp	.L266
.L265:
	movl	$0,%eax
.L266:
	pushl	%eax
	pushl	%ecx
	pushl	%ebx
	pushl	%edx
	pushl	$_s4
	call	_printf
	leal	20(%esp),%esp
	pushl	$-1294967268
	pushl	$-77
	pushl	$-1294967270
	pushl	$-75
	call	_f2f
	leal	16(%esp),%esp
	pushl	_l_ul
	pushl	_ui_ul
	pushl	_i_ul
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	movl	$-79,%edi
	movl	$-1294967266,%esi
	movl	$-81,%ebx
	movl	$0,%edx
	cmpl	$40,%esi
	jae	.L271
.L270:
	movl	$1,%ecx
	jmp	.L272
.L271:
	movl	$0,%ecx
.L272:
	cmpl	$41,%ebx
	jae	.L274
.L273:
	movl	$1,%eax
	jmp	.L275
.L274:
	movl	$0,%eax
.L275:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	cmpl	$39,%edi
	jae	.L277
.L276:
	movl	$1,%edx
	jmp	.L278
.L277:
	movl	$0,%edx
.L278:
	cmpl	$40,%esi
	jae	.L280
.L279:
	movl	$1,%ecx
	jmp	.L281
.L280:
	movl	$0,%ecx
.L281:
	cmpl	$41,%ebx
	jae	.L283
.L282:
	movl	$1,%eax
	jmp	.L284
.L283:
	movl	$0,%eax
.L284:
	pushl	%eax
	pushl	%ecx
	pushl	%edx
	pushl	$_s3
	call	_printf
	leal	16(%esp),%esp
	pushl	$-81
	pushl	$-1294967266
	pushl	$-79
	call	_f2ul
	leal	12(%esp),%esp
	pushl	_ui_l
	pushl	_i_l
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
	movl	$-82,%esi
	movl	$-1294967263,%ebx
	movl	$1,%ecx
	cmpl	$43,%ebx
	jae	.L289
.L288:
	movl	$1,%eax
	jmp	.L290
.L289:
	movl	$0,%eax
.L290:
	pushl	%eax
	pushl	%ecx
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
	cmpl	$42,%esi
	jge	.L292
.L291:
	movl	$1,%ecx
	jmp	.L293
.L292:
	movl	$0,%ecx
.L293:
	cmpl	$43,%ebx
	jae	.L295
.L294:
	movl	$1,%eax
	jmp	.L296
.L295:
	movl	$0,%eax
.L296:
	pushl	%eax
	pushl	%ecx
	pushl	$_s2
	call	_printf
	leal	12(%esp),%esp
	pushl	$-1294967263
	pushl	$-82
	call	_f2l
	leal	8(%esp),%esp
	pushl	_i_ui
	pushl	$_s1
	call	_printf
	leal	8(%esp),%esp
	movl	$-84,%ebx
	movl	$0,%eax
	pushl	%eax
	pushl	$_s1
	call	_printf
	leal	8(%esp),%esp
	cmpl	$44,%ebx
	jae	.L301
.L300:
	movl	$1,%eax
	jmp	.L302
.L301:
	movl	$0,%eax
.L302:
	pushl	%eax
	pushl	$_s1
	call	_printf
	leal	8(%esp),%esp
	pushl	$-84
	call	_f2ui
	leal	4(%esp),%esp
	movl	$0,%eax
	popl	%edi
	popl	%esi
	popl	%ebx
	leave
	ret

	.data
	.align	1
	.global	_s1
_s1:
	.byte	37
	.byte	100
	.byte	10
	.byte	0
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
	.align	1
	.global	_s3
_s3:
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
	.align	1
	.global	_s5
_s5:
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
	.byte	32
	.byte	37
	.byte	100
	.byte	10
	.byte	0
	.align	4
	.global	_d_i
_d_i:
	.long	0
	.align	4
	.global	_d_ui
_d_ui:
	.long	1
	.align	4
	.global	_d_l
_d_l:
	.long	0
	.align	4
	.global	_d_ul
_d_ul:
	.long	1
	.align	4
	.global	_d_f
_d_f:
	.long	0
	.align	4
	.global	_f_i
_f_i:
	.long	0
	.align	4
	.global	_f_ui
_f_ui:
	.long	1
	.align	4
	.global	_f_l
_f_l:
	.long	0
	.align	4
	.global	_f_ul
_f_ul:
	.long	1
	.align	4
	.global	_ul_i
_ul_i:
	.long	1
	.align	4
	.global	_ul_ui
_ul_ui:
	.long	1
	.align	4
	.global	_ul_l
_ul_l:
	.long	1
	.align	4
	.global	_l_i
_l_i:
	.long	0
	.align	4
	.global	_l_ui
_l_ui:
	.long	1
	.align	4
	.global	_ui_i
_ui_i:
	.long	1
	.align	4
	.global	_i_d
_i_d:
	.long	1
	.align	4
	.global	_ui_d
_ui_d:
	.long	0
	.align	4
	.global	_l_d
_l_d:
	.long	1
	.align	4
	.global	_ul_d
_ul_d:
	.long	0
	.align	4
	.global	_f_d
_f_d:
	.long	1
	.align	4
	.global	_i_f
_i_f:
	.long	1
	.align	4
	.global	_ui_f
_ui_f:
	.long	0
	.align	4
	.global	_l_f
_l_f:
	.long	1
	.align	4
	.global	_ul_f
_ul_f:
	.long	0
	.align	4
	.global	_i_ul
_i_ul:
	.long	0
	.align	4
	.global	_ui_ul
_ui_ul:
	.long	0
	.align	4
	.global	_l_ul
_l_ul:
	.long	0
	.align	4
	.global	_i_l
_i_l:
	.long	1
	.align	4
	.global	_ui_l
_ui_l:
	.long	0
	.align	4
	.global	_i_ui
_i_ui:
	.long	0
	.text
	.align	4
.LC1:
	.long	0x0,0x40240000 /* 10.0 */
	.align	4
.LC2:
	.long	0x0,0x41f00000 /* 4.294967296E9 */
	.align	4
.LC3:
	.long	0x0,0x40260000 /* 11.0 */
	.align	4
.LC4:
	.long	0x0,0x40280000 /* 12.0 */
	.align	4
.LC5:
	.long	0x0,0x402a0000 /* 13.0 */
	.align	4
.LC6:
	.long	0x55e63c,0x402c0000 /* 14.00000001 */
	.align	4
.LC7:
	.long	0x41700000 /* 15.0 */
	.align	4
.LC8:
	.long	0x4f800000 /* 4.294967296E9 */
	.align	4
.LC9:
	.long	0x41800000 /* 16.0 */
	.align	4
.LC10:
	.long	0x41880000 /* 17.0 */
	.align	4
.LC11:
	.long	0x41900000 /* 18.0 */
	.align	4
.LC12:
	.long	0x0,0x403e0000 /* 30.0 */
	.align	4
.LC13:
	.long	0x0,0x403f0000 /* 31.0 */
	.align	4
.LC14:
	.long	0x0,0x40400000 /* 32.0 */
	.align	4
.LC15:
	.long	0x0,0x40408000 /* 33.0 */
	.align	4
.LC16:
	.long	0x15798f,0x40410000 /* 34.00000001 */
	.align	4
.LC17:
	.long	0x420c0000 /* 35.0 */
	.align	4
.LC18:
	.long	0x42100000 /* 36.0 */
	.align	4
.LC19:
	.long	0x42140000 /* 37.0 */
	.align	4
.LC20:
	.long	0x42180000 /* 38.0 */
	.align	4
.LC21:
	.long	0x41600000 /* 14.0 */
	.align	4
.LC22:
	.long	0x42080000 /* 34.0 */
