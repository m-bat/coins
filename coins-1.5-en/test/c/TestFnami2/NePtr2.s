 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */
	.text
	.align	1
_string.22:
	.byte	84
	.byte	0
	.align	1
_string.24:
	.byte	45
	.byte	0
	.align	1
_string.30:
	.byte	10
	.byte	0

	.align	4
	.global	_f0
_f0:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	movl	8(%ebp),%ebx
	cmpl	$_s,%ebx
	je	.L4
.L3:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L5
.L4:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L5:
	cmpl	$_s+4,%ebx
	je	.L7
.L6:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L8
.L7:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L8:
	cmpl	$0,%ebx
	je	.L10
.L9:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L11
.L10:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L11:
	pushl	$_string.30
	call	_printf
	leal	4(%esp),%esp
.L12:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_g0
_g0:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	movl	8(%ebp),%ebx
	cmpl	$_u,%ebx
	je	.L16
.L15:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L17
.L16:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L17:
	cmpl	$_u,%ebx
	je	.L19
.L18:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L20
.L19:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L20:
	cmpl	$0,%ebx
	je	.L22
.L21:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L23
.L22:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L23:
	pushl	$_string.30
	call	_printf
	leal	4(%esp),%esp
.L24:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_h0
_h0:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	movl	8(%ebp),%ebx
	cmpl	$_a+8,%ebx
	je	.L28
.L27:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L29
.L28:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L29:
	cmpl	$_a+16,%ebx
	je	.L31
.L30:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L32
.L31:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L32:
	cmpl	$0,%ebx
	je	.L34
.L33:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L35
.L34:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L35:
	pushl	$_string.30
	call	_printf
	leal	4(%esp),%esp
.L36:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_f1
_f1:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	movl	8(%ebp),%ebx
	cmpl	$_s,%ebx
	je	.L40
.L39:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L41
.L40:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L41:
	cmpl	$_s+4,%ebx
	je	.L43
.L42:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L44
.L43:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L44:
	cmpl	$0,%ebx
	je	.L46
.L45:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L47
.L46:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L47:
	pushl	$_string.30
	call	_printf
	leal	4(%esp),%esp
.L48:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_g1
_g1:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	movl	8(%ebp),%ebx
	cmpl	$_u,%ebx
	je	.L52
.L51:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L53
.L52:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L53:
	cmpl	$_u,%ebx
	je	.L55
.L54:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L56
.L55:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L56:
	cmpl	$0,%ebx
	je	.L58
.L57:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L59
.L58:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L59:
	pushl	$_string.30
	call	_printf
	leal	4(%esp),%esp
.L60:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_h1
_h1:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	movl	8(%ebp),%ebx
	cmpl	$_a+8,%ebx
	je	.L64
.L63:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L65
.L64:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L65:
	cmpl	$_a+16,%ebx
	je	.L67
.L66:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L68
.L67:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L68:
	cmpl	$0,%ebx
	je	.L70
.L69:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L71
.L70:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L71:
	pushl	$_string.30
	call	_printf
	leal	4(%esp),%esp
.L72:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_op
_op:
	pushl	%ebp
	movl	%esp,%ebp
	movl	8(%ebp),%ecx
	movl	12(%ebp),%eax
	cmpl	%eax,%ecx
	je	.L76
.L75:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L77
.L76:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L77:
	leave
	ret


	.align	4
	.global	_f2
_f2:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	movl	8(%ebp),%ebx
	pushl	$_s
	pushl	%ebx
	call	_op
	leal	8(%esp),%esp
	pushl	$_s+4
	pushl	%ebx
	call	_op
	leal	8(%esp),%esp
	pushl	$0
	pushl	%ebx
	call	_op
	leal	8(%esp),%esp
	pushl	$_string.30
	call	_printf
	leal	4(%esp),%esp
.L80:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_g2
_g2:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	movl	8(%ebp),%ebx
	pushl	$_u
	pushl	%ebx
	call	_op
	leal	8(%esp),%esp
	pushl	$_u
	pushl	%ebx
	call	_op
	leal	8(%esp),%esp
	pushl	$0
	pushl	%ebx
	call	_op
	leal	8(%esp),%esp
	pushl	$_string.30
	call	_printf
	leal	4(%esp),%esp
.L83:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_h2
_h2:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	movl	8(%ebp),%ebx
	pushl	$_a+8
	pushl	%ebx
	call	_op
	leal	8(%esp),%esp
	pushl	$_a+16
	pushl	%ebx
	call	_op
	leal	8(%esp),%esp
	pushl	$0
	pushl	%ebx
	call	_op
	leal	8(%esp),%esp
	pushl	$_string.30
	call	_printf
	leal	4(%esp),%esp
.L86:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_main0g
_main0g:
	pushl	%ebp
	movl	%esp,%ebp
	movl	$_s,%eax
	cmpl	$_s,%eax
	je	.L90
.L89:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L91
.L90:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L91:
	movl	$_s,%eax
	cmpl	$_s+4,%eax
	je	.L93
.L92:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L94
.L93:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L94:
	movl	$_s,%eax
	cmpl	$0,%eax
	je	.L96
.L95:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L97
.L96:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L97:
	pushl	$_string.30
	call	_printf
	leal	4(%esp),%esp
	movl	$_s+4,%eax
	cmpl	$_s,%eax
	je	.L99
.L98:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L100
.L99:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L100:
	movl	$_s+4,%eax
	cmpl	$_s+4,%eax
	je	.L102
.L101:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L103
.L102:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L103:
	movl	$_s+4,%eax
	cmpl	$0,%eax
	je	.L105
.L104:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L106
.L105:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L106:
	pushl	$_string.30
	call	_printf
	leal	4(%esp),%esp
	movl	$0,%eax
	cmpl	$_s,%eax
	je	.L108
.L107:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L109
.L108:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L109:
	movl	$0,%eax
	cmpl	$_s+4,%eax
	je	.L111
.L110:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L114
.L111:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L114:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
	pushl	$_string.30
	call	_printf
	leal	4(%esp),%esp
	movl	$_u,%eax
	cmpl	$_s,%eax
	je	.L117
.L116:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L118
.L117:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L118:
	movl	$_u,%eax
	cmpl	$_s+4,%eax
	je	.L120
.L119:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L121
.L120:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L121:
	movl	$_u,%eax
	cmpl	$0,%eax
	je	.L123
.L122:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L124
.L123:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L124:
	pushl	$_string.30
	call	_printf
	leal	4(%esp),%esp
	movl	$_u,%eax
	cmpl	$_u,%eax
	je	.L126
.L125:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L127
.L126:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L127:
	movl	$_u,%eax
	cmpl	$_u,%eax
	je	.L129
.L128:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L130
.L129:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L130:
	movl	$_u,%eax
	cmpl	$0,%eax
	je	.L132
.L131:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L133
.L132:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L133:
	pushl	$_string.30
	call	_printf
	leal	4(%esp),%esp
	movl	$_u,%eax
	cmpl	$_u,%eax
	je	.L135
.L134:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L136
.L135:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L136:
	movl	$_u,%eax
	cmpl	$_u,%eax
	je	.L138
.L137:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L139
.L138:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L139:
	movl	$_u,%eax
	cmpl	$0,%eax
	je	.L141
.L140:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L142
.L141:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L142:
	pushl	$_string.30
	call	_printf
	leal	4(%esp),%esp
	movl	$0,%eax
	cmpl	$_u,%eax
	je	.L144
.L143:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L145
.L144:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L145:
	movl	$0,%eax
	cmpl	$_u,%eax
	je	.L147
.L146:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L150
.L147:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L150:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
	pushl	$_string.30
	call	_printf
	leal	4(%esp),%esp
	movl	$_a+8,%eax
	cmpl	$_a+8,%eax
	je	.L153
.L152:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L154
.L153:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L154:
	movl	$_a+8,%eax
	cmpl	$_a+16,%eax
	je	.L156
.L155:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L157
.L156:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L157:
	movl	$_a+8,%eax
	cmpl	$0,%eax
	je	.L159
.L158:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L160
.L159:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L160:
	pushl	$_string.30
	call	_printf
	leal	4(%esp),%esp
	movl	$_a+16,%eax
	cmpl	$_a+8,%eax
	je	.L162
.L161:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L163
.L162:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L163:
	movl	$_a+16,%eax
	cmpl	$_a+16,%eax
	je	.L165
.L164:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L166
.L165:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L166:
	movl	$_a+16,%eax
	cmpl	$0,%eax
	je	.L168
.L167:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L169
.L168:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L169:
	pushl	$_string.30
	call	_printf
	leal	4(%esp),%esp
	movl	$0,%eax
	cmpl	$_a+8,%eax
	je	.L171
.L170:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L172
.L171:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L172:
	movl	$0,%eax
	cmpl	$_a+16,%eax
	je	.L174
.L173:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L177
.L174:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L177:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
	pushl	$_string.30
	call	_printf
	leal	4(%esp),%esp
.L179:
	leave
	ret


	.align	4
	.global	_main0l
_main0l:
	pushl	%ebp
	movl	%esp,%ebp
	subl	$28,%esp
	movl	$0,-28(%ebp)
	movl	$10,-24(%ebp)
	movl	$20,-20(%ebp)
	movl	$30,-16(%ebp)
	leal	-8(%ebp),%ecx
	leal	-8(%ebp),%eax
	cmpl	%eax,%ecx
	je	.L183
.L182:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L184
.L183:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L184:
	leal	-8(%ebp),%ecx
	leal	-4(%ebp),%eax
	cmpl	%eax,%ecx
	je	.L186
.L185:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L187
.L186:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L187:
	leal	-8(%ebp),%eax
	cmpl	$0,%eax
	je	.L189
.L188:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L190
.L189:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L190:
	pushl	$_string.30
	call	_printf
	leal	4(%esp),%esp
	leal	-4(%ebp),%ecx
	leal	-8(%ebp),%eax
	cmpl	%eax,%ecx
	je	.L192
.L191:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L193
.L192:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L193:
	leal	-4(%ebp),%ecx
	leal	-4(%ebp),%eax
	cmpl	%eax,%ecx
	je	.L195
.L194:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L196
.L195:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L196:
	leal	-4(%ebp),%eax
	cmpl	$0,%eax
	je	.L198
.L197:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L199
.L198:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L199:
	pushl	$_string.30
	call	_printf
	leal	4(%esp),%esp
	leal	-8(%ebp),%eax
	cmpl	$0,%eax
	je	.L201
.L200:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L202
.L201:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L202:
	leal	-4(%ebp),%eax
	cmpl	$0,%eax
	je	.L204
.L203:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L207
.L204:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L207:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
	pushl	$_string.30
	call	_printf
	leal	4(%esp),%esp
	leal	-12(%ebp),%ecx
	leal	-8(%ebp),%eax
	cmpl	%eax,%ecx
	je	.L210
.L209:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L211
.L210:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L211:
	leal	-12(%ebp),%ecx
	leal	-4(%ebp),%eax
	cmpl	%eax,%ecx
	je	.L213
.L212:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L214
.L213:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L214:
	leal	-12(%ebp),%eax
	cmpl	$0,%eax
	je	.L216
.L215:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L217
.L216:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L217:
	pushl	$_string.30
	call	_printf
	leal	4(%esp),%esp
	leal	-12(%ebp),%ecx
	leal	-12(%ebp),%eax
	cmpl	%eax,%ecx
	je	.L219
.L218:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L220
.L219:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L220:
	leal	-12(%ebp),%ecx
	leal	-12(%ebp),%eax
	cmpl	%eax,%ecx
	je	.L222
.L221:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L223
.L222:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L223:
	leal	-12(%ebp),%eax
	cmpl	$0,%eax
	je	.L225
.L224:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L226
.L225:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L226:
	pushl	$_string.30
	call	_printf
	leal	4(%esp),%esp
	leal	-12(%ebp),%ecx
	leal	-12(%ebp),%eax
	cmpl	%eax,%ecx
	je	.L228
.L227:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L229
.L228:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L229:
	leal	-12(%ebp),%ecx
	leal	-12(%ebp),%eax
	cmpl	%eax,%ecx
	je	.L231
.L230:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L232
.L231:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L232:
	leal	-12(%ebp),%eax
	cmpl	$0,%eax
	je	.L234
.L233:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L235
.L234:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L235:
	pushl	$_string.30
	call	_printf
	leal	4(%esp),%esp
	leal	-12(%ebp),%eax
	cmpl	$0,%eax
	je	.L237
.L236:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L238
.L237:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L238:
	leal	-12(%ebp),%eax
	cmpl	$0,%eax
	je	.L240
.L239:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L243
.L240:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L243:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
	pushl	$_string.30
	call	_printf
	leal	4(%esp),%esp
	leal	-20(%ebp),%ecx
	leal	-20(%ebp),%eax
	cmpl	%eax,%ecx
	je	.L246
.L245:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L247
.L246:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L247:
	leal	-20(%ebp),%ecx
	leal	-12(%ebp),%eax
	cmpl	%eax,%ecx
	je	.L249
.L248:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L250
.L249:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L250:
	leal	-20(%ebp),%eax
	cmpl	$0,%eax
	je	.L252
.L251:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L253
.L252:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L253:
	pushl	$_string.30
	call	_printf
	leal	4(%esp),%esp
	leal	-12(%ebp),%ecx
	leal	-20(%ebp),%eax
	cmpl	%eax,%ecx
	je	.L255
.L254:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L256
.L255:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L256:
	leal	-12(%ebp),%ecx
	leal	-12(%ebp),%eax
	cmpl	%eax,%ecx
	je	.L258
.L257:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L259
.L258:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L259:
	leal	-12(%ebp),%eax
	cmpl	$0,%eax
	je	.L261
.L260:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L262
.L261:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L262:
	pushl	$_string.30
	call	_printf
	leal	4(%esp),%esp
	leal	-20(%ebp),%eax
	cmpl	$0,%eax
	je	.L264
.L263:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L265
.L264:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L265:
	leal	-12(%ebp),%eax
	cmpl	$0,%eax
	je	.L267
.L266:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L270
.L267:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L270:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
	pushl	$_string.30
	call	_printf
	leal	4(%esp),%esp
.L272:
	leave
	ret


	.align	4
	.global	_main1
_main1:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	movl	$_s,%ebx
	cmpl	$_s,%ebx
	je	.L276
.L275:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L277
.L276:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L277:
	cmpl	$_s,%ebx
	je	.L279
.L278:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L280
.L279:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L280:
	movl	$_s,%ebx
	cmpl	$_s+4,%ebx
	je	.L282
.L281:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L283
.L282:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L283:
	cmpl	$_s+4,%ebx
	je	.L285
.L284:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L286
.L285:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L286:
	movl	$_s,%ebx
	cmpl	$0,%ebx
	je	.L288
.L287:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L289
.L288:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L289:
	cmpl	$0,%ebx
	je	.L291
.L290:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L292
.L291:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L292:
	pushl	$_string.30
	call	_printf
	leal	4(%esp),%esp
	movl	$_s+4,%ebx
	cmpl	$_s,%ebx
	je	.L294
.L293:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L295
.L294:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L295:
	cmpl	$_s,%ebx
	je	.L297
.L296:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L298
.L297:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L298:
	movl	$_s+4,%ebx
	cmpl	$_s+4,%ebx
	je	.L300
.L299:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L301
.L300:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L301:
	cmpl	$_s+4,%ebx
	je	.L303
.L302:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L304
.L303:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L304:
	movl	$_s+4,%ebx
	cmpl	$0,%ebx
	je	.L306
.L305:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L307
.L306:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L307:
	cmpl	$0,%ebx
	je	.L309
.L308:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L310
.L309:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L310:
	pushl	$_string.30
	call	_printf
	leal	4(%esp),%esp
	movl	$0,%ebx
	movl	$0,%eax
	cmpl	$_s,%eax
	je	.L312
.L311:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L313
.L312:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L313:
	cmpl	$_s,%ebx
	je	.L315
.L314:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L316
.L315:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L316:
	movl	$0,%ebx
	movl	$0,%eax
	cmpl	$_s+4,%eax
	je	.L318
.L317:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L319
.L318:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L319:
	cmpl	$_s+4,%ebx
	je	.L321
.L320:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L322
.L321:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L322:
	movl	$0,%ebx
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
	cmpl	$0,%ebx
	je	.L327
.L326:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L328
.L327:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L328:
	pushl	$_string.30
	call	_printf
	leal	4(%esp),%esp
	movl	$_u,%ebx
	cmpl	$_s,%ebx
	je	.L330
.L329:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L331
.L330:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L331:
	cmpl	$_s,%ebx
	je	.L333
.L332:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L334
.L333:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L334:
	movl	$_u,%ebx
	cmpl	$_s+4,%ebx
	je	.L336
.L335:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L337
.L336:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L337:
	cmpl	$_s+4,%ebx
	je	.L339
.L338:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L340
.L339:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L340:
	movl	$_u,%ebx
	cmpl	$0,%ebx
	je	.L342
.L341:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L343
.L342:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L343:
	cmpl	$0,%ebx
	je	.L345
.L344:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L346
.L345:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L346:
	pushl	$_string.30
	call	_printf
	leal	4(%esp),%esp
.L347:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_main2
_main2:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	movl	$_u,%ebx
	cmpl	$_u,%ebx
	je	.L351
.L350:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L352
.L351:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L352:
	cmpl	$_u,%ebx
	je	.L354
.L353:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L355
.L354:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L355:
	movl	$_u,%ebx
	cmpl	$_u,%ebx
	je	.L357
.L356:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L358
.L357:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L358:
	cmpl	$_u,%ebx
	je	.L360
.L359:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L361
.L360:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L361:
	movl	$_u,%ebx
	cmpl	$0,%ebx
	je	.L363
.L362:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L364
.L363:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L364:
	cmpl	$0,%ebx
	je	.L366
.L365:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L367
.L366:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L367:
	pushl	$_string.30
	call	_printf
	leal	4(%esp),%esp
	movl	$_u,%ebx
	cmpl	$_u,%ebx
	je	.L369
.L368:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L370
.L369:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L370:
	cmpl	$_u,%ebx
	je	.L372
.L371:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L373
.L372:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L373:
	movl	$_u,%ebx
	cmpl	$_u,%ebx
	je	.L375
.L374:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L376
.L375:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L376:
	cmpl	$_u,%ebx
	je	.L378
.L377:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L379
.L378:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L379:
	movl	$_u,%ebx
	cmpl	$0,%ebx
	je	.L381
.L380:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L382
.L381:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L382:
	cmpl	$0,%ebx
	je	.L384
.L383:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L385
.L384:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L385:
	pushl	$_string.30
	call	_printf
	leal	4(%esp),%esp
	movl	$0,%ebx
	movl	$0,%eax
	cmpl	$_u,%eax
	je	.L387
.L386:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L388
.L387:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L388:
	cmpl	$_u,%ebx
	je	.L390
.L389:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L391
.L390:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L391:
	movl	$0,%ebx
	movl	$0,%eax
	cmpl	$_u,%eax
	je	.L393
.L392:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L394
.L393:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L394:
	cmpl	$_u,%ebx
	je	.L396
.L395:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L397
.L396:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L397:
	movl	$0,%ebx
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
	cmpl	$0,%ebx
	je	.L402
.L401:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L403
.L402:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L403:
	pushl	$_string.30
	call	_printf
	leal	4(%esp),%esp
.L404:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_main3
_main3:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	movl	$_a+8,%ebx
	cmpl	$_a+8,%ebx
	je	.L408
.L407:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L409
.L408:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L409:
	cmpl	$_a+8,%ebx
	je	.L411
.L410:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L412
.L411:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L412:
	movl	$_a+8,%ebx
	cmpl	$_a+16,%ebx
	je	.L414
.L413:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L415
.L414:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L415:
	cmpl	$_a+16,%ebx
	je	.L417
.L416:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L418
.L417:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L418:
	movl	$_a+8,%ebx
	cmpl	$0,%ebx
	je	.L420
.L419:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L421
.L420:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L421:
	cmpl	$0,%ebx
	je	.L423
.L422:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L424
.L423:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L424:
	pushl	$_string.30
	call	_printf
	leal	4(%esp),%esp
	movl	$_a+16,%ebx
	cmpl	$_a+8,%ebx
	je	.L426
.L425:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L427
.L426:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L427:
	cmpl	$_a+8,%ebx
	je	.L429
.L428:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L430
.L429:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L430:
	movl	$_a+16,%ebx
	cmpl	$_a+16,%ebx
	je	.L432
.L431:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L433
.L432:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L433:
	cmpl	$_a+16,%ebx
	je	.L435
.L434:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L436
.L435:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L436:
	movl	$_a+16,%ebx
	cmpl	$0,%ebx
	je	.L438
.L437:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L439
.L438:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L439:
	cmpl	$0,%ebx
	je	.L441
.L440:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L442
.L441:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L442:
	pushl	$_string.30
	call	_printf
	leal	4(%esp),%esp
	movl	$0,%ebx
	movl	$0,%eax
	cmpl	$_a+8,%eax
	je	.L444
.L443:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L445
.L444:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L445:
	cmpl	$_a+8,%ebx
	je	.L447
.L446:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L448
.L447:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L448:
	movl	$0,%ebx
	movl	$0,%eax
	cmpl	$_a+16,%eax
	je	.L450
.L449:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L451
.L450:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L451:
	cmpl	$_a+16,%ebx
	je	.L453
.L452:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L454
.L453:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L454:
	movl	$0,%ebx
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
	cmpl	$0,%ebx
	je	.L459
.L458:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L460
.L459:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L460:
	pushl	$_string.30
	call	_printf
	leal	4(%esp),%esp
.L461:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_main4
_main4:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	movl	$_s,%ebx
	cmpl	$_s,%ebx
	je	.L465
.L464:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L466
.L465:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L466:
	cmpl	$_s,%ebx
	je	.L468
.L467:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L469
.L468:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L469:
	movl	$_s,%ebx
	cmpl	$_s+4,%ebx
	je	.L471
.L470:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L472
.L471:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L472:
	cmpl	$_s+4,%ebx
	je	.L474
.L473:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L475
.L474:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L475:
	movl	$_s,%ebx
	cmpl	$0,%ebx
	je	.L477
.L476:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L478
.L477:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L478:
	cmpl	$0,%ebx
	je	.L480
.L479:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L481
.L480:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L481:
	pushl	$_string.30
	call	_printf
	leal	4(%esp),%esp
	movl	$_s+4,%ebx
	cmpl	$_s,%ebx
	je	.L483
.L482:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L484
.L483:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L484:
	cmpl	$_s,%ebx
	je	.L486
.L485:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L487
.L486:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L487:
	movl	$_s+4,%ebx
	cmpl	$_s+4,%ebx
	je	.L489
.L488:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L490
.L489:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L490:
	cmpl	$_s+4,%ebx
	je	.L492
.L491:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L493
.L492:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L493:
	movl	$_s+4,%ebx
	cmpl	$0,%ebx
	je	.L495
.L494:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L496
.L495:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L496:
	cmpl	$0,%ebx
	je	.L498
.L497:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L499
.L498:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L499:
	pushl	$_string.30
	call	_printf
	leal	4(%esp),%esp
	movl	$0,%ebx
	movl	$_s,%eax
	cmpl	$0,%eax
	je	.L501
.L500:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L502
.L501:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L502:
	cmpl	$_s,%ebx
	je	.L504
.L503:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L505
.L504:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L505:
	movl	$0,%ebx
	movl	$_s+4,%eax
	cmpl	$0,%eax
	je	.L507
.L506:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L508
.L507:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L508:
	cmpl	$_s+4,%ebx
	je	.L510
.L509:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L511
.L510:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L511:
	movl	$0,%ebx
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
	cmpl	$0,%ebx
	je	.L516
.L515:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L517
.L516:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L517:
	pushl	$_string.30
	call	_printf
	leal	4(%esp),%esp
	movl	$_u,%ebx
	cmpl	$_s,%ebx
	je	.L519
.L518:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L520
.L519:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L520:
	cmpl	$_s,%ebx
	je	.L522
.L521:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L523
.L522:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L523:
	movl	$_u,%ebx
	cmpl	$_s+4,%ebx
	je	.L525
.L524:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L526
.L525:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L526:
	cmpl	$_s+4,%ebx
	je	.L528
.L527:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L529
.L528:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L529:
	movl	$_u,%ebx
	cmpl	$0,%ebx
	je	.L531
.L530:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L532
.L531:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L532:
	cmpl	$0,%ebx
	je	.L534
.L533:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L535
.L534:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L535:
	pushl	$_string.30
	call	_printf
	leal	4(%esp),%esp
.L536:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_main5
_main5:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	movl	$_u,%ebx
	cmpl	$_u,%ebx
	je	.L540
.L539:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L541
.L540:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L541:
	cmpl	$_u,%ebx
	je	.L543
.L542:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L544
.L543:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L544:
	movl	$_u,%ebx
	cmpl	$_u,%ebx
	je	.L546
.L545:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L547
.L546:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L547:
	cmpl	$_u,%ebx
	je	.L549
.L548:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L550
.L549:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L550:
	movl	$_u,%ebx
	cmpl	$0,%ebx
	je	.L552
.L551:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L553
.L552:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L553:
	cmpl	$0,%ebx
	je	.L555
.L554:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L556
.L555:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L556:
	pushl	$_string.30
	call	_printf
	leal	4(%esp),%esp
	movl	$_u,%ebx
	cmpl	$_u,%ebx
	je	.L558
.L557:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L559
.L558:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L559:
	cmpl	$_u,%ebx
	je	.L561
.L560:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L562
.L561:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L562:
	movl	$_u,%ebx
	cmpl	$_u,%ebx
	je	.L564
.L563:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L565
.L564:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L565:
	cmpl	$_u,%ebx
	je	.L567
.L566:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L568
.L567:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L568:
	movl	$_u,%ebx
	cmpl	$0,%ebx
	je	.L570
.L569:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L571
.L570:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L571:
	cmpl	$0,%ebx
	je	.L573
.L572:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L574
.L573:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L574:
	pushl	$_string.30
	call	_printf
	leal	4(%esp),%esp
	movl	$0,%ebx
	movl	$_u,%eax
	cmpl	$0,%eax
	je	.L576
.L575:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L577
.L576:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L577:
	cmpl	$_u,%ebx
	je	.L579
.L578:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L580
.L579:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L580:
	movl	$0,%ebx
	movl	$_u,%eax
	cmpl	$0,%eax
	je	.L582
.L581:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L583
.L582:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L583:
	cmpl	$_u,%ebx
	je	.L585
.L584:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L586
.L585:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L586:
	movl	$0,%ebx
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
	cmpl	$0,%ebx
	je	.L591
.L590:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L592
.L591:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L592:
	pushl	$_string.30
	call	_printf
	leal	4(%esp),%esp
.L593:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_main6
_main6:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	%ebx
	movl	$_a+8,%ebx
	cmpl	$_a+8,%ebx
	je	.L597
.L596:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L598
.L597:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L598:
	cmpl	$_a+8,%ebx
	je	.L600
.L599:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L601
.L600:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L601:
	movl	$_a+8,%ebx
	cmpl	$_a+16,%ebx
	je	.L603
.L602:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L604
.L603:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L604:
	cmpl	$_a+16,%ebx
	je	.L606
.L605:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L607
.L606:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L607:
	movl	$_a+8,%ebx
	cmpl	$0,%ebx
	je	.L609
.L608:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L610
.L609:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L610:
	cmpl	$0,%ebx
	je	.L612
.L611:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L613
.L612:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L613:
	pushl	$_string.30
	call	_printf
	leal	4(%esp),%esp
	movl	$_a+16,%ebx
	cmpl	$_a+8,%ebx
	je	.L615
.L614:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L616
.L615:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L616:
	cmpl	$_a+8,%ebx
	je	.L618
.L617:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L619
.L618:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L619:
	movl	$_a+16,%ebx
	cmpl	$_a+16,%ebx
	je	.L621
.L620:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L622
.L621:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L622:
	cmpl	$_a+16,%ebx
	je	.L624
.L623:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L625
.L624:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L625:
	movl	$_a+16,%ebx
	cmpl	$0,%ebx
	je	.L627
.L626:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L628
.L627:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L628:
	cmpl	$0,%ebx
	je	.L630
.L629:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L631
.L630:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L631:
	pushl	$_string.30
	call	_printf
	leal	4(%esp),%esp
	movl	$0,%ebx
	movl	$_a+8,%eax
	cmpl	$0,%eax
	je	.L633
.L632:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L634
.L633:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L634:
	cmpl	$_a+8,%ebx
	je	.L636
.L635:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L637
.L636:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L637:
	movl	$0,%ebx
	movl	$_a+16,%eax
	cmpl	$0,%eax
	je	.L639
.L638:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L640
.L639:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L640:
	cmpl	$_a+16,%ebx
	je	.L642
.L641:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L643
.L642:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L643:
	movl	$0,%ebx
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
	cmpl	$0,%ebx
	je	.L648
.L647:
	pushl	$_string.22
	call	_printf
	leal	4(%esp),%esp
	jmp	.L649
.L648:
	pushl	$_string.24
	call	_printf
	leal	4(%esp),%esp
.L649:
	pushl	$_string.30
	call	_printf
	leal	4(%esp),%esp
.L650:
	popl	%ebx
	leave
	ret


	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	call	_main0g
	call	_main0l
	call	_main1
	call	_main2
	call	_main3
	call	_main4
	call	_main5
	call	_main6
	pushl	$_s
	call	_f0
	leal	4(%esp),%esp
	pushl	$_s+4
	call	_f0
	leal	4(%esp),%esp
	pushl	$0
	call	_f0
	leal	4(%esp),%esp
	pushl	$_u
	call	_f0
	leal	4(%esp),%esp
	pushl	$_u
	call	_g0
	leal	4(%esp),%esp
	pushl	$_u
	call	_g0
	leal	4(%esp),%esp
	pushl	$0
	call	_g0
	leal	4(%esp),%esp
	pushl	$_a+8
	call	_h0
	leal	4(%esp),%esp
	pushl	$_a+16
	call	_h0
	leal	4(%esp),%esp
	pushl	$0
	call	_h0
	leal	4(%esp),%esp
	pushl	$_s
	call	_f1
	leal	4(%esp),%esp
	pushl	$_s+4
	call	_f1
	leal	4(%esp),%esp
	pushl	$0
	call	_f1
	leal	4(%esp),%esp
	pushl	$_u
	call	_f1
	leal	4(%esp),%esp
	pushl	$_u
	call	_g1
	leal	4(%esp),%esp
	pushl	$_u
	call	_g1
	leal	4(%esp),%esp
	pushl	$0
	call	_g1
	leal	4(%esp),%esp
	pushl	$_a+8
	call	_h1
	leal	4(%esp),%esp
	pushl	$_a+16
	call	_h1
	leal	4(%esp),%esp
	pushl	$0
	call	_h1
	leal	4(%esp),%esp
	pushl	$_s
	call	_f2
	leal	4(%esp),%esp
	pushl	$_s+4
	call	_f2
	leal	4(%esp),%esp
	pushl	$0
	call	_f2
	leal	4(%esp),%esp
	pushl	$_u
	call	_f2
	leal	4(%esp),%esp
	pushl	$_u
	call	_g2
	leal	4(%esp),%esp
	pushl	$_u
	call	_g2
	leal	4(%esp),%esp
	pushl	$0
	call	_g2
	leal	4(%esp),%esp
	pushl	$_a+8
	call	_h2
	leal	4(%esp),%esp
	pushl	$_a+16
	call	_h2
	leal	4(%esp),%esp
	pushl	$0
	call	_h2
	leal	4(%esp),%esp
	movl	$0,%eax
	leave
	ret

	.data
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
	.align	4
	.global	_a
_a:
	.long	0
	.long	10
	.long	20
	.long	30
	.comm	_s,8
	.comm	_u,4
