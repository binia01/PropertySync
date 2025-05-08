import { PrismaClient, Role, PropertyStatus, AppointmentStatus } from '@prisma/client';
import * as argon2 from 'argon2';

const prisma = new PrismaClient();

async function main() {
  // Clear existing data (optional for dev)
  await prisma.appointment.deleteMany();
  await prisma.property.deleteMany();
  await prisma.user.deleteMany();

  // Hash a default password
  const password = await argon2.hash('password123');

  // Create Sellers
  const seller1 = await prisma.user.create({
    data: {
      email: 'seller1@example.com',
      firstname: 'Alice',
      lastname: 'Seller',
      hash: password,
      role: Role.SELLER,
    },
  });

  const seller2 = await prisma.user.create({
    data: {
      email: 'seller2@example.com',
      firstname: 'Bob',
      lastname: 'Seller',
      hash: password,
      role: Role.SELLER,
    },
  });

  // Create Properties by Sellers
  const property1 = await prisma.property.create({
    data: {
      title: 'Modern Apartment in NYC',
      description: 'A beautiful modern apartment in the heart of New York.',
      price: 500000,
      location: 'New York',
      status: PropertyStatus.ACTIVE,
      sellerId: seller1.id,
    },
  });

  const property2 = await prisma.property.create({
    data: {
      title: 'Cozy House in LA',
      description: 'A cozy and quiet place to live.',
      price: 750000,
      location: 'Los Angeles',
      status: PropertyStatus.ACTIVE,
      sellerId: seller2.id,
    },
  });

  // Create Buyers
  const buyer1 = await prisma.user.create({
    data: {
      email: 'buyer1@example.com',
      firstname: 'Charlie',
      lastname: 'Buyer',
      hash: password,
      role: Role.BUYER,
    },
  });

  const buyer2 = await prisma.user.create({
    data: {
      email: 'buyer2@example.com',
      firstname: 'Dana',
      lastname: 'Buyer',
      hash: password,
      role: Role.BUYER,
    },
  });

  const buyer3 = await prisma.user.create({
    data: {
      email: 'buyer3@example.com',
      firstname: 'Elliot',
      lastname: 'Buyer',
      hash: password,
      role: Role.BUYER,
    },
  });

  // Create Appointments for some buyers
  await prisma.appointment.create({
    data: {
      startTime: new Date(Date.now() + 1000 * 60 * 60 * 24), // 1 day from now
      Date: new Date(),
      propertyId: property1.id,
      buyerId: buyer1.id,
      sellerId: seller1.id,
      status: AppointmentStatus.PENDING,
    },
  });

  await prisma.appointment.create({
    data: {
      startTime: new Date(Date.now() + 1000 * 60 * 60 * 48), // 2 days from now
      Date: new Date(),
      propertyId: property2.id,
      buyerId: buyer2.id,
      sellerId: seller2.id,
      status: AppointmentStatus.CONFIRMED,
    },
  });

  // No appointments for buyer3
}

main()
  .then(() => {
    console.log('✅ Seed data created.');
    return prisma.$disconnect();
  })
  .catch((e) => {
    console.error('❌ Error while seeding:', e);
    return prisma.$disconnect().finally(() => process.exit(1));
  });
