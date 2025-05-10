import React from 'react';

interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: 'default' | 'ghost';
  size?: 'default' | 'icon';
}

export const Button: React.FC<ButtonProps> = ({ children, variant = 'default', size = 'default', ...props }) => {
  const base = 'rounded font-semibold px-4 py-2';
  const variants = {
    default: 'bg-blue-600 text-white hover:bg-blue-700',
    ghost: 'bg-transparent text-blue-600 hover:bg-blue-100',
  };
  const sizes = {
    default: '',
    icon: 'w-8 h-8 p-0 flex items-center justify-center',
  };

  return (
    <button
      {...props}
      className={`${base} ${variants[variant]} ${sizes[size]} ${props.className ?? ''}`}
    >
      {children}
    </button>
  );
};
