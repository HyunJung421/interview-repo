import React from 'react';

export const Select: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  return <div className="relative w-full">{children}</div>;
};

export const SelectTrigger: React.FC<{ children: React.ReactNode; className?: string }> = ({ children, className }) => {
  return (
    <button className={`border border-gray-300 rounded px-3 py-2 w-full text-left ${className}`}>
      {children}
    </button>
  );
};

export const SelectValue: React.FC<{ placeholder: string }> = ({ placeholder }) => {
  return <span className="text-gray-500">{placeholder}</span>;
};

export const SelectContent: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  return <div className="absolute bg-white border mt-1 rounded shadow z-10 w-full">{children}</div>;
};

export const SelectItem: React.FC<{ value: string; children: React.ReactNode }> = ({ children }) => {
  return (
    <div className="px-3 py-2 hover:bg-gray-100 cursor-pointer">
      {children}
    </div>
  );
};
