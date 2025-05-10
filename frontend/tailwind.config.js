/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        babyblue: '#d2e7ff',
        navy: '#27316e',
        lime: '#c2e7d1',
      }
    },
  },
  plugins: [],
}

